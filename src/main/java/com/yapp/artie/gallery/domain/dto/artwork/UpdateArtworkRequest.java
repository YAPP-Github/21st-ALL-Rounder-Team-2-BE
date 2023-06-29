package com.yapp.artie.gallery.domain.dto.artwork;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "전시 작품 수정 Request")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateArtworkRequest {

  @Schema(description = "작가 이름")
  private String artist;

  @Schema(description = "작품명")
  private String name;

  @Valid
  @Schema(description = "작품 할당 태그")
  private List<@NotBlank String> tags;

  @Builder
  public UpdateArtworkRequest(String artist, String name, List<String> tags) {
    this.artist = artist;
    this.name = name;
    this.tags = tags;
  }
}
