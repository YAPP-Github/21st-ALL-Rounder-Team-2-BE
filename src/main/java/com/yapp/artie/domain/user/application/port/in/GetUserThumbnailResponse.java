package com.yapp.artie.domain.user.application.port.in;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "마이 페이지 조회 Response")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetUserThumbnailResponse {

  @Schema(description = "사용자 이름")
  private String name;

  @Schema(description = "전시 개수")
  private int exhibitCount;

  public GetUserThumbnailResponse(String name, int exhibitCount) {
    this.name = name;
    this.exhibitCount = exhibitCount;
  }
}