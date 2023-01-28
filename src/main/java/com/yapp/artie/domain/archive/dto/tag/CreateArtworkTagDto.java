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
  @Schema(description = "태그 ID. 기존에 존재한 태그를 작품에 추가할 경우 제공되어야함.")
  private Long tagId;

  @NotNull
  @NotBlank
  @Schema(description = "태그 이름. 새롭게 태그를 생성하여 작품에 추가할 경우 태그 이름만 제공되어야함.")
  private String tagName;
}
