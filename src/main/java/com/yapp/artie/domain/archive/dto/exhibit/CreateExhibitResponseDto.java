package com.yapp.artie.domain.archive.dto.exhibit;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Schema(description = "전시 생성 Response")
@RequiredArgsConstructor
public class CreateExhibitResponseDto {

  @Schema(description = "전시 아이디")
  private final Long id;
}
