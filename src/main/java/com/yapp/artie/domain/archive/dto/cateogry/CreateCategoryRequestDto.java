package com.yapp.artie.domain.archive.dto.cateogry;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "카테고리 생성 Request")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateCategoryRequestDto {

  @Schema(description = "카테고리 명")
  private String name;

  public CreateCategoryRequestDto(String name) {
    this.name = name;
  }
}
