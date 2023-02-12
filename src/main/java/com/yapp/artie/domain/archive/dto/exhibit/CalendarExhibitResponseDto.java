package com.yapp.artie.domain.archive.dto.exhibit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@Schema(description = "월별 전시 조회 Response")
@RequiredArgsConstructor
public class CalendarExhibitResponseDto {

  @NonNull
  @Schema(description = "연도(year)", required = true)
  private final int year;

  @NonNull
  @Schema(description = "월(month)", required = true)
  private final int month;

  @NonNull
  @Schema(description = "일(day)", required = true)
  private final int day;

  @Schema(description = "대표 이미지")
  private final String imageURL;

  @Schema(description = "해당 날짜의 전시 수")
  private final Long postNum;
}
