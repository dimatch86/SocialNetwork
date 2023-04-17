package ru.skillbox.zerone.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.skillbox.zerone.backend.exception.PostCreationException;
import ru.skillbox.zerone.backend.exception.PostNotFoundException;
import ru.skillbox.zerone.backend.exception.UserAndAuthorEqualsException;
import ru.skillbox.zerone.backend.mapstruct.PostMapper;
import ru.skillbox.zerone.backend.model.dto.request.PostRequestDTO;
import ru.skillbox.zerone.backend.model.dto.response.CommonListResponseDTO;
import ru.skillbox.zerone.backend.model.dto.response.CommonResponseDTO;
import ru.skillbox.zerone.backend.model.dto.response.PostDTO;
import ru.skillbox.zerone.backend.model.entity.Post;
import ru.skillbox.zerone.backend.model.entity.User;
import ru.skillbox.zerone.backend.repository.PostRepository;
import ru.skillbox.zerone.backend.util.CurrentUserUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
  private final PostRepository postRepository;
  private final SearchService searchService;
  private final PostMapper postMapper;
  private final NotificationService notificationService;


  @Transactional
  public CommonResponseDTO<PostDTO> createPost(int id, long publishDate, PostRequestDTO postRequestDTO) {

    User user = CurrentUserUtils.getCurrentUser();

    if (user.getId() != id) {
      throw new PostCreationException("Попытка публикации неизвестным пользователем");
    }

    Post post = Post.builder()
        .postText(postRequestDTO.getPostText())
        .title(postRequestDTO.getTitle())
        .author(user)
        .time(publishDate == 0 ? LocalDateTime.now()
            : Instant.ofEpochMilli(publishDate).atZone(ZoneId.systemDefault()).toLocalDateTime())
        .build();

    postRepository.save(post);
    notificationService.savePost(post);

    return commonResponseDTO(post);
  }

  private PostDTO getPostsDTO(Post post) {

    return postMapper.postToPostsDTO(post);
  }

  private List<PostDTO> getPost4Response(List<Post> posts) {

    List<PostDTO> postDataList = new ArrayList<>();
    posts.forEach(post -> postDataList.add(getPostsDTO(post)));

    return postDataList;
  }

  public CommonListResponseDTO<PostDTO> getFeeds(int offset, int itemPerPage) {

    long myId = CurrentUserUtils.getCurrentUser().getId();
    Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
    Page<Post> pageablePostList = postRepository.getPostsForFeeds(myId, pageable);

    return commonListResponseDTO(offset, itemPerPage, pageablePostList);
  }

  public CommonResponseDTO<PostDTO> getPostById(long id) {

    return commonResponseDTO(getPostFromRepo(id));
  }


  public CommonListResponseDTO<PostDTO> getAuthorWall(long id, int offset, int itemPerPage) {

    Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
    Page<Post> pageablePostList = postRepository.getPostsForUsersWall(id, pageable);

    return commonListResponseDTO(offset, itemPerPage, pageablePostList);
  }

  public CommonListResponseDTO<PostDTO> getPosts(String text, String author, String tag, Long dateFrom, int offset, int itemPerPage) {

    Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
    Page<Post> pageablePostList = searchService.searchPosts(text, author, tag, dateFrom, pageable);

    return commonListResponseDTO(offset, itemPerPage, pageablePostList);
  }

  @Transactional
  public CommonResponseDTO<PostDTO> deletePostById(long id) {

    Post post = getPostFromRepo(id);

    throwExceptionIfAuthorNotEqualsUser(post);

    post.setIsDeleted(true);
    postRepository.saveAndFlush(post);
    return commonResponseDTO(post);
  }

  @Transactional
  public CommonResponseDTO<PostDTO> putPostIdRecover(long id) {

    Post post = getPostFromRepo(id);

    throwExceptionIfAuthorNotEqualsUser(post);

    post.setIsDeleted(false);
    postRepository.saveAndFlush(post);
    return commonResponseDTO(post);
  }

  @Transactional
  public CommonResponseDTO<PostDTO> putPostById(long id, Long publishDate, PostRequestDTO requestBody) {

    Post post = getPostFromRepo(id);

    throwExceptionIfAuthorNotEqualsUser(post);

    post.setTitle(requestBody.getTitle());
    post.setPostText(requestBody.getPostText());
    post.setTime(Instant.ofEpochMilli(publishDate == 0 ? System.currentTimeMillis() : publishDate).atZone(ZoneId.systemDefault()).toLocalDateTime());
    postRepository.saveAndFlush(post);
    return commonResponseDTO(post);
  }

  private CommonResponseDTO<PostDTO> commonResponseDTO(Post post) {

    return CommonResponseDTO.<PostDTO>builder()
        .timestamp(LocalDateTime.now())
        .data(getPostsDTO(post))
        .build();
  }

  private CommonListResponseDTO<PostDTO> commonListResponseDTO(int offset, int itemPerPage, Page<Post> pageablePostList) {

    return CommonListResponseDTO.<PostDTO>builder()
        .total(pageablePostList.getTotalElements())
        .perPage(itemPerPage)
        .offset(offset)
        .data(getPost4Response(pageablePostList.toList()))
        .build();
  }

  private Post getPostFromRepo(long id) {
    return postRepository.findById(id)
        .orElseThrow(() -> new PostNotFoundException("Пост с указанным id не найден"));
  }

  private void throwExceptionIfAuthorNotEqualsUser(Post post) {

    User user = CurrentUserUtils.getCurrentUser();
    if (!user.getId().equals(post.getAuthor().getId())) {
      throw new UserAndAuthorEqualsException("Попытка редактирования неизвестным пользователем");
    }
  }
}
