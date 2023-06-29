package com.yapp.artie.gallery.dto.artwork;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@Schema(description = "작품 상세 페이지의 작품 소형 썸네일 이미지 Response")
@RequiredArgsConstructor
public class ArtworkImageThumbnailResponse {

  @NonNull
  @Schema(description = "작품 ID", required = true)
  private Long id;

  @NonNull
  @Schema(description = "작품 이미지", required = true)
  private String imageURL;
}
