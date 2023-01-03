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
public class ExposureCondition {

  @Column(nullable = false, name = "seq")
  private int sequence;

  @Column(nullable = false, columnDefinition = "boolean default false", name = "is_main_image")
  private boolean isMain;
}
