package com.yapp.archiveServer.archive.domain.artwork;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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
