package com.yapp.artie.domain.archive.dto.cateogry;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "카테고리 생성 Request")
public class CreateCategoryRequestDto {

  @Schema(description = "카테고리 명")
  private final String name;
}
