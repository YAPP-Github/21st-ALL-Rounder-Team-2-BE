package com.yapp.artie.domain.archive.dto.exhibit;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "전시 수정 Request")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateExhibitRequestDto {

  @Schema(description = "수정할 이름")
  private String name;

  @Schema(description = "수정할 날짜")
  private LocalDate postDate;

  public UpdateExhibitRequestDto(String name, LocalDate postDate) {
    this.name = name;
    this.postDate = postDate;
  }
}
