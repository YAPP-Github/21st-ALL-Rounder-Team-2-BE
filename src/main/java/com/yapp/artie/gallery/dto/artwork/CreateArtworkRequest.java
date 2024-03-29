package com.yapp.artie.gallery.dto.artwork;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@Schema(description = "전시 작품 생성 Request")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateArtworkRequest {

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
  private List<@NotBlank String> tags;

  @Builder
  public CreateArtworkRequest(@NonNull Long postId, @NonNull String imageUri, String artist,
      String name, List<String> tags) {
    this.postId = postId;
    this.imageUri = imageUri;
    this.artist = artist;
    this.name = name;
    this.tags = tags;
  }
}
