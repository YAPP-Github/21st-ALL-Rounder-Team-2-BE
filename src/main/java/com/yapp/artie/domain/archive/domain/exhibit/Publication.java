package com.yapp.artie.domain.archive.domain.exhibit;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Publication {

  @Column(nullable = false, columnDefinition = "boolean default false")
  private boolean isPublished = false;

  private LocalDate publishedAt;

  public void publish() {
    this.isPublished = true;
    this.publishedAt = LocalDate.now();
  }
}
