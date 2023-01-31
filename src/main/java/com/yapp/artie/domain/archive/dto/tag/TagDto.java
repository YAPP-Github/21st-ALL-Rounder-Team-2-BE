package com.yapp.artie.domain.archive.dto.tag;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "태그 Response")
public class TagDto {

  @NonNull
  @Schema(description = "태그 아이디")
  private final Long id;

  @NonNull
  @Schema(description = "태그 명")
  private final String name;
}
