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

  @Column(nullable = false, columnDefinition = "boolean default true")
  private boolean isDraft = true;

  private LocalDate publishedAt;

  public void publish() {
    this.isDraft = false;
    this.publishedAt = LocalDate.now();
  }
}
