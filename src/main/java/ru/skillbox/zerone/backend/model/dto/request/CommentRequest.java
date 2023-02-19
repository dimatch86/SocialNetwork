package ru.skillbox.zerone.backend.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.skillbox.zerone.backend.model.dto.response.ImageDTO;


import java.util.List;

@Data
public class CommentRequest {
  @JsonProperty("parent_id")
  private Integer parentId;
  @JsonProperty("comment_text")
  private String commentText;
  @JsonProperty("images")
  private List<ImageDTO> images;
}
