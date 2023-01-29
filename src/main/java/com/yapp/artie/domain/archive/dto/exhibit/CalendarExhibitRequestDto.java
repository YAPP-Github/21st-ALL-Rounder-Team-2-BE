package com.yapp.artie.domain.archive.dto.exhibit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Schema(description = "월별 전시 조회 Request")
@RequiredArgsConstructor
public class CalendarExhibitRequestDto {

  @Schema(description = "연도(year)")
  private final int year;

  @Schema(description = "월(month)")
  private final int month;
}

