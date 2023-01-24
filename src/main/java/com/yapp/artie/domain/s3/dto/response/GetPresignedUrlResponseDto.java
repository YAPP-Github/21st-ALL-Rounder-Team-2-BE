package com.yapp.artie.domain.s3.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Schema(description = "이미지 업로드 Presigned URL 반환 Response")
@RequiredArgsConstructor
public class GetPresignedUrlResponseDto {

  @NotNull
  @Schema(description = "Presigned URL 목록")
  public final List<presignedUrlDataDto> url;
}
