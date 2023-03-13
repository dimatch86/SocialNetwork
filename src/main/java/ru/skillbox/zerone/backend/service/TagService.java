package ru.skillbox.zerone.backend.service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.skillbox.zerone.backend.mapstruct.TagMapper;
import ru.skillbox.zerone.backend.model.dto.request.TagDTO;
import ru.skillbox.zerone.backend.model.dto.response.CommonListResponseDTO;
import ru.skillbox.zerone.backend.model.dto.response.CommonResponseDTO;
import ru.skillbox.zerone.backend.model.dto.response.MessageResponseDTO;
import ru.skillbox.zerone.backend.model.entity.Tag;
import ru.skillbox.zerone.backend.repository.TagRepository;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TagService {

  private final TagRepository tagRepository;
  private final TagMapper tagMapper;
  @Transactional
  public CommonResponseDTO<TagDTO> addTag(TagDTO tagDTO) {

    Tag tag = tagMapper.tagDTOToTag(tagDTO);

    tagRepository.save(tag);

    return CommonResponseDTO.<TagDTO>builder()
        .data(tagDTO)
        .build();
  }

  @Transactional
  public CommonResponseDTO<MessageResponseDTO> deleteTag (Long id) {

    tagRepository.deleteById(id);

    return CommonResponseDTO.<MessageResponseDTO>builder().data(new MessageResponseDTO("Ok")).build();
  }

  @Transactional
  public CommonListResponseDTO<TagDTO> getAllTags(String tag, Integer offset, Integer itemPerPage) {
    Pageable pageable = PageRequest.of(offset, itemPerPage);
    Page<Tag> pageableTagList;

    if (tag.isEmpty()) {
      pageableTagList = tagRepository.findAll(pageable);
    } else {
      pageableTagList = tagRepository.findByTag(tag, pageable);
    }

    List<TagDTO> tagDTOList = pageableTagList.map(tagMapper::tagToTagDTO).toList();


    return CommonListResponseDTO.<TagDTO>builder()
        .total(pageableTagList.getTotalElements())
        .perPage(itemPerPage)
        .offset(offset)
        .data(tagDTOList)
        .build();

  }

}
