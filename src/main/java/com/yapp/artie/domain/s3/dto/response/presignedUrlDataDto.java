package com.yapp.artie.domain.s3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Schema(description = "이미지 업로드 Presigned URL 반환 Response")
@RequiredArgsConstructor
public class presignedUrlDataDto {

  @NotNull
  @Schema(description = "Presigned URL 목록")
  public final String url;

  @NotNull
  @Schema(description = "요청 이미지 이름")
  public final String originalName;

  @NotNull
  @Schema(description = "S3 업로드 이미지 키 이름")
  public final String imageKey;
}
