package ru.skillbox.zerone.backend.model.dto.socket.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TypingDataDTO {
  @JsonProperty("author")
  private long authorId;
  @JsonProperty("dialog")
  private long dialogId;
}
