package com.yapp.artie.domain.archive.dto.artwork;

import com.yapp.artie.domain.archive.dto.tag.CreateArtworkTagDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "전시 생성 Request")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateArtworkRequestDto {

  @NotNull
  @Positive
  @Schema(description = "전시 ID")
  private Long postId;

  @NotNull
  @NotBlank
  @Schema(description = "s3 이미지 URI. S3 Presigned URL 발급 후 반환한 imageKey 값")
  private String imageUri;

  @Schema(description = "작가 이름")
  private String artist;

  @Schema(description = "작품명")
  private String name;

  @Valid
  @Schema(description = "작품 할당 태그")
  private List<CreateArtworkTagDto> tags = new ArrayList<>();
}
