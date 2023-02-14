package com.yapp.artie.domain.archive.dto.exhibit;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "전시 생성 Request")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateExhibitRequestDto {

  @Schema(description = "전시명")
  private String name;

  @Schema(description = "카테고리 아이디")
  private Long categoryId;

  @Schema(description = "관람 날짜")
  private LocalDate postDate;

  @Schema(description = "전시 링크", nullable = true)
  private String attachedLink;

  public CreateExhibitRequestDto(String name, Long categoryId, LocalDate postDate,
      String attachedLink) {
    this.name = name;
    this.categoryId = categoryId;
    this.postDate = postDate;
    this.attachedLink = attachedLink;
  }
}
