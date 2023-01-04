package com.yapp.artie.domain.archive.domain.category;

import java.io.Serializable;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HomeCategoryId implements Serializable {

  private Long categoryId;
  private Long userId;
}
