package com.yapp.artie.s3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Schema(description = "발급된 이미지 업로드 Presigned URL 정보 Response")
@RequiredArgsConstructor
public class GetPresignedUrlResponseDto {

  @NotNull
  @Schema(description = "발급된 S3 Presigned URL 정보 리스트")
  public final List<presignedUrlDataDto> url;
}
