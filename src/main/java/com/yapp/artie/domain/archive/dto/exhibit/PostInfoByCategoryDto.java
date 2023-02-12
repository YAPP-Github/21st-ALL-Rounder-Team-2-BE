package com.yapp.artie.domain.archive.dto.exhibit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Getter
@Schema(description = "카테고리 페이지 내 전시 목록 썸네일")
@RequiredArgsConstructor
public class PostInfoByCategoryDto {

  @NonNull
  @Schema(description = "전시 아이디")
  private Long id;

  @NonNull
  @Schema(description = "전시명")
  private String name;

  @NonNull
  @Schema(description = "대표 이미지")
  private String mainImage;
}
