package com.yapp.artie.gallery.dto.exhibition;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@Schema(description = "특정 날짜의 전시 정보 Response")
@RequiredArgsConstructor
public class ExhibitionByDateResponse {

  @NonNull
  @Schema(description = "전시 ID", required = true)
  private final Long postId;

  @NonNull
  @Schema(description = "전시 이름", required = true)
  private final String postName;
}
