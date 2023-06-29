package com.yapp.artie.gallery.domain.dto.artwork;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "전시 작품 다중 생성 Request")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateArtworkBatchRequest {

  @Valid
  @Schema(description = "s3 이미지 URI 목록. S3 Presigned URL 발급 후 반환한 imageKey 값 목록")
  private List<@NotBlank String> imageUriList;
}
