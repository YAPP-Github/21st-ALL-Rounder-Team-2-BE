package com.yapp.artie.notice.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(nullable = false)
  private String title;

  @Column(columnDefinition = "text")
  private String contents;

  @NotNull
  @Column(nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdAt;

  private Notice(String title, String contents) {
    this.title = title;
    this.contents = contents;
    this.createdAt = LocalDateTime.now();
  }

  public static Notice create(String title, String contents) {
    return new Notice(title, contents);
  }
}
