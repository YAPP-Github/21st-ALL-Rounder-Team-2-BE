package com.yapp.artie.domain.archive.dto.artwork;

import com.yapp.artie.domain.archive.dto.tag.TagDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@Schema(description = "작품 목록의 작품 썸네일 정보")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtworkInfoDto {

  @NonNull
  @Schema(description = "작품 ID", required = true)
  private Long id;

  @NonNull
  @Schema(description = "작품 이미지", required = true)
  private String imageURL;

  @NonNull
  @Schema(description = "작품명", required = true, defaultValue = "작품명 미입력")
  private String name;

  @NonNull
  @Schema(description = "관람 날짜", required = true, defaultValue = "작가명 미입력")
  private String artist;

  @Valid
  @NonNull
  @Schema(description = "감정 태그 목록", required = true)
  private List<TagDto> tags;

  @Builder
  public ArtworkInfoDto(@NonNull Long id, @NonNull String imageURL, String name,
      String artist, List<TagDto> tags) {
    this.id = id;
    this.imageURL = imageURL;
    this.name = name == null ? "작품명 미입력" : name;
    this.artist = artist == null ? "작품명 미입력" : artist;
    this.tags = tags == null ? new ArrayList<>() : tags;
  }
}
