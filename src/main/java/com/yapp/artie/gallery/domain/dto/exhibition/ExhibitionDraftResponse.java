package com.yapp.artie.gallery.domain.dto.exhibition;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@Schema(description = "임시 전시 정보 Response")
@RequiredArgsConstructor
public class ExhibitionDraftResponse {

  @Schema(description = "전시 아이디")
  private final Long id;

  @Schema(description = "전시명")
  private final String name;

  @Schema(description = "관람 날짜")
  private final LocalDate postDate;

  @Schema(description = "전시 링크")
  private final String attachedLink;

  @Schema(description = "임시 저장 여부")
  private final boolean isPublished;
}
