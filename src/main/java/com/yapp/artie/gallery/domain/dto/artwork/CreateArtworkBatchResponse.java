package com.yapp.artie.gallery.domain.dto.artwork;


import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Schema(description = "전시 작품 다중 생성 Response")
@RequiredArgsConstructor
public class CreateArtworkBatchResponse {

  @Schema(description = "전시 작품 아이디 목록")
  private final List<Long> idList;
}
