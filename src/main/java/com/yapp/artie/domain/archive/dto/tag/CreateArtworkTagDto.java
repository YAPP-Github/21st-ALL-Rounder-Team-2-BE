package com.yapp.artie.domain.archive.dto.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;

@Getter
@Schema(description = "전시 작품 추가시 할당될 태그 정보")
public class CreateArtworkTagDto {

  @Positive
  @Schema(description = "태그 ID")
  private Long tagId;

  @NotNull
  @NotBlank
  @Schema(description = "태그 이름")
  private String tagName;
}
