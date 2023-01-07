package com.yapp.artie.domain.archive.dto.cateogry;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "카테고리 수정 Request")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateCategoryRequestDto {

  @Schema(description = "수정할 이름")
  private String name;
}