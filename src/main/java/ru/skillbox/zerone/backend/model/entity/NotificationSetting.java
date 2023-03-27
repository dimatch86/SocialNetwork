package ru.skillbox.zerone.backend.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification_setting",
    indexes = @Index(name = "notification_setting_user_id_idx", columnList = "user_id")
)
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
  @JoinColumn(name = "user_id", referencedColumnName = "id",
      foreignKey = @ForeignKey(name = "notification_setting_user_fk")
  )
  private User user;

  @NotNull
  @Builder.Default
  @Column(name = "post_enabled", columnDefinition = "boolean default true")
  private Boolean postEnabled = true;

  @NotNull
  @Builder.Default
  @Column(name = "post_comment_enabled", columnDefinition = "boolean default true")
  private Boolean postCommentEnabled = true;

  @NotNull
  @Builder.Default
  @Column(name = "comment_comment_enabled", columnDefinition = "boolean default true")
  private Boolean commentCommentEnabled = true;

  @NotNull
  @Builder.Default
  @Column(name = "friend_request_enabled", columnDefinition = "boolean default true")
  private Boolean friendRequestEnabled = true;

  @NotNull
  @Builder.Default
  @Column(name = "messages_enabled", columnDefinition = "boolean default true")
  private Boolean messagesEnabled = true;

  @NotNull
  @Builder.Default
  @Column(name = "friend_birthday_enabled", columnDefinition = "boolean default true")
  private Boolean friendBirthdayEnabled = true;
}
