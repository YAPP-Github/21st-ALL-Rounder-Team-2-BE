package com.yapp.artie.archive.domain.exhibit;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExhibitContents {

  @Column(nullable = false)
  private String name;

  @Column(columnDefinition = "text")
  private String review;

  private String attachedLink;

  @Column(nullable = false, name = "post_date")
  private LocalDate date;
}
