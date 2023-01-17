package com.yapp.artie.domain.archive.dto.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "태그 Response")
public class TagDto {

  @Schema(description = "태그 아이디")
  private final Long id;

  @Schema(description = "태그 명")
  private final String name;
}
