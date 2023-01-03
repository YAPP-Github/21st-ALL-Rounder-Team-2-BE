package com.yapp.artie.domain.archive.domain.artwork;

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
public class ArtworkContents {

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String artist;

  @Column(nullable = false)
  private String url;

  @Column(columnDefinition = "text")
  private String description;
}
