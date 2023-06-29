package com.yapp.artie.gallery.dto.artwork;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Getter
@Schema(description = "작품 목록의 작품 썸네일 정보 Response")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtworkThumbnailResponse {

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

  @Builder
  public ArtworkThumbnailResponse(@NonNull Long id, @NonNull String imageURL, String name,
      String artist) {
    this.id = id;
    this.imageURL = imageURL;
    this.name = name == null ? "작품명 미입력" : name;
    this.artist = artist == null ? "작품명 미입력" : artist;
  }

  public static class Page extends PageImpl<ArtworkThumbnailResponse> {

    public Page(List<ArtworkThumbnailResponse> content,
        Pageable pageable, long total) {
      super(content, pageable, total);
    }
  }
}
