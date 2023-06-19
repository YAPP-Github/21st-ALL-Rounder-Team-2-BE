package com.yapp.artie.domain.exhibition.domain.dto.artwork;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@Schema(description = "작품 목록의 작품 썸네일 정보")
@RequiredArgsConstructor
public class ArtworkImageThumbnailResponse {

  @NonNull
  @Schema(description = "작품 ID", required = true)
  private Long id;

  @NonNull
  @Schema(description = "작품 이미지", required = true)
  private String imageURL;
}
