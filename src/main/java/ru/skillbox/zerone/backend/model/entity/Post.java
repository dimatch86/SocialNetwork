package ru.skillbox.zerone.backend.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

import java.util.Set;
@Entity
@Table(name = "post",
    indexes = @Index(name = "post_author_id_idx", columnList = "author_id")
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @NotNull
  @Builder.Default
  @Column(name = "time", columnDefinition = "timestamp without time zone")
  private LocalDateTime time = LocalDateTime.now();

  @NotNull
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "author_id", referencedColumnName = "id",
      foreignKey = @ForeignKey(name = "post_author_fk")
  )
  private User author;

  @NotNull
  @NotBlank
  @Column(name = "title")
  private String title;

  @NotNull
  @NotBlank
  @Column(name = "post_text", columnDefinition = "text")
  private String postText;

  @NotNull
  @Builder.Default
  @Column(name = "update_date", columnDefinition = "timestamp without time zone")
  @UpdateTimestamp
  private LocalDateTime updateTime = LocalDateTime.now();

  @NotNull
  @Builder.Default
  @Column(name = "is_blocked", columnDefinition = "boolean default false")
  private Boolean isBlocked = false;

  @NotNull
  @Builder.Default
  @Column(name = "is_deleted", columnDefinition = "boolean default false")
  private Boolean isDeleted = false;
//  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "post")
//  private Set<PostComment> comments;
//  @ManyToMany(fetch = FetchType.LAZY)
//  private Set<Tag> tags;
}
