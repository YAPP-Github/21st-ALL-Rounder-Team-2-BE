package com.yapp.artie.domain.archive.dto.exhibit;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@Schema(description = "월별 전시 조회 Response")
@RequiredArgsConstructor
public class CalendarExhibitResponseDto {

  @Schema(description = "관람 날짜", required = true)
  private final LocalDate postDate;

  @NonNull
  @Schema(description = "전시 ID", required = true)
  private final Long postId;

  @Schema(description = "대표 이미지")
  private final String imageURL;

  @Schema(description = "해당 날짜의 전시 수")
  private final Long postNum;
}
