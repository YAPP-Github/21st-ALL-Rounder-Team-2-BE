package com.yapp.artie.domain.archive.dto.artwork;

import com.yapp.artie.domain.archive.dto.tag.CreateArtworkTagDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.Valid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "전시 작품 생성 Request")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateArtworkRequestDto {

  @Schema(description = "작가 이름")
  private String artist;

  @Schema(description = "작품명")
  private String name;

  @Valid
  @Schema(description = "작품 할당 태그")
  private List<CreateArtworkTagDto> tags;

  @Builder
  public UpdateArtworkRequestDto(String artist, String name, List<CreateArtworkTagDto> tags) {
    this.artist = artist;
    this.name = name;
    this.tags = tags;
  }
}
