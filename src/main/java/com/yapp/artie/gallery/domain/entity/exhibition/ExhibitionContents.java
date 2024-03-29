package com.yapp.artie.gallery.domain.entity.exhibition;

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
public class ExhibitionContents {

  @Column(nullable = false)
  private String name;

  @Column(columnDefinition = "text")
  private String review;

  private String attachedLink;

  @Column(nullable = false, name = "post_date")
  private LocalDate date;
}
