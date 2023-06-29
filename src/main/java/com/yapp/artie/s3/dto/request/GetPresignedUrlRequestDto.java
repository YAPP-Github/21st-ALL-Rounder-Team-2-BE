package com.yapp.artie.s3.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "이미지 업로드를 위한 S3 Presigned URL 발급 요청 Request")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetPresignedUrlRequestDto {

  @NotNull
  @Schema(description = "이미지 이름")
  private List<@NotBlank String> imageNames;
}
