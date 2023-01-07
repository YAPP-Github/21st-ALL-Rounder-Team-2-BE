package com.yapp.artie.domain.archive.dto.cateogry;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Schema(description = "카테고리 생성 Response")
@RequiredArgsConstructor
public class CreateCategoryResponseDto {

  @Schema(description = "카테고리 아이디")
  private final Long id;
}
