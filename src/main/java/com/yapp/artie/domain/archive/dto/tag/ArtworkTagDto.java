package com.yapp.artie.domain.archive.dto.tag;

import com.yapp.artie.domain.archive.domain.tag.ArtworkTagId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "이미지태그 Response")
public class ArtworkTagDto {

  @Schema(description = "이미지태그 아이디")
  private final ArtworkTagId id;

  @Schema(description = "이미지태그 순서")
  private final int seq;
}
