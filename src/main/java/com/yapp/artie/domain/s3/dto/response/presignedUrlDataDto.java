package com.yapp.artie.domain.s3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Schema(description = "각 이미지에 대해 발급된 S3 업로드 Presigned URL Response")
@RequiredArgsConstructor
public class presignedUrlDataDto {

  @NotNull
  @Schema(description = "발급된 S3 업로드 Presigned URL")
  public final String url;

  @NotNull
  @Schema(description = "요청한 이미지 이름")
  public final String originalName;

  @NotNull
  @Schema(description = "S3 업로드 이미지 키 이름")
  public final String imageKey;
}
