package com.yapp.archiveServer.archive.domain.category;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HomeCategoryId implements Serializable {

  private Long categoryId;
  private Long userId;
}
