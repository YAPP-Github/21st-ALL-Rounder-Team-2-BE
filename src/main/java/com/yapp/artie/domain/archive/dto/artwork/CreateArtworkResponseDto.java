package com.yapp.artie.domain.archive.dto.artwork;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Schema(description = "전시 작품 생성 Response")
@RequiredArgsConstructor
public class CreateArtworkResponseDto {

  @Schema(description = "전시 작품 아이디")
  private final Long id;
}
