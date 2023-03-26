package ru.skillbox.zerone.backend.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification_setting")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSetting {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @NotNull
  @Builder.Default
  @Column(name = "post_enabled", columnDefinition = "boolean default false")
  private Boolean postEnabled = true;

  @NotNull
  @Builder.Default
  @Column(name = "post_comment_enabled", columnDefinition = "boolean default false")
  private Boolean postCommentEnabled = true;

  @NotNull
  @Builder.Default
  @Column(name = "comment_comment_enabled", columnDefinition = "boolean default false")
  private Boolean commentCommentEnabled = true;

  @NotNull
  @Builder.Default
  @Column(name = "friend_request_enabled", columnDefinition = "boolean default false")
  private Boolean friendRequestEnabled = true;

  @NotNull
  @Builder.Default
  @Column(name = "messages_enabled", columnDefinition = "boolean default false")
  private Boolean messagesEnabled = true;

  @NotNull
  @Builder.Default
  @Column(name = "friend_birthday_enabled")
  private Boolean friendBirthdayEnabled = true;
}
