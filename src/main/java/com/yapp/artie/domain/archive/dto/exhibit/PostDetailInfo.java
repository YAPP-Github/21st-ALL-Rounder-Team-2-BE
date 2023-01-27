package com.yapp.artie.domain.archive.dto.exhibit;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@Schema(description = "전시 Response")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDetailInfo {

  @NonNull
  @Schema(description = "전시 아이디", required = true)
  private Long id;

  @NonNull
  @Schema(description = "전시명", required = true)
  private String name;

  @NonNull
  @Schema(description = "관람 날짜", required = true)
  private LocalDate postDate;

  @Schema(description = "임시 저장 여부")
  private boolean isPublished;

  @NonNull
  @Schema(description = "카테고리 아이디", required = true)
  private Long categoryId;

  @NonNull
  @Schema(description = "카테고리 명", required = true)
  private String categoryName;

  @Schema(description = "대표 이미지")
  private String mainImage;

  @Builder
  public PostDetailInfo(@NonNull Long id, @NonNull String name, @NonNull LocalDate postDate,
      boolean isPublished, @NonNull Long categoryId, @NonNull String categoryName,
      String mainImage) {
    this.id = id;
    this.name = name;
    this.postDate = postDate;
    this.isPublished = isPublished;
    this.categoryId = categoryId;
    this.categoryName = categoryName;
    this.mainImage = mainImage;
  }
}
