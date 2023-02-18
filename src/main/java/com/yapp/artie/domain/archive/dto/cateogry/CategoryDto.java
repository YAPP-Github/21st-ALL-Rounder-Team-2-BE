package com.yapp.artie.domain.archive.dto.cateogry;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "카테고리 Response")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryDto {

  @Schema(description = "카테고리 아이디")
  private Long id;

  @Schema(description = "카테고리 명")
  private String name;

  @Schema(description = "카테고리 순서")
  private int sequence;

  @Schema(description = "카테고리 별 전시수")
  private int postNum;

  public CategoryDto(Long id, String name, int sequence) {
    this.id = id;
    this.name = name;
    this.sequence = sequence;
    this.postNum = 0;
  }

  public CategoryDto(Long id, String name, int sequence, int postNum) {
    this.id = id;
    this.name = name;
    this.sequence = sequence;
    this.postNum = postNum;
  }
}
