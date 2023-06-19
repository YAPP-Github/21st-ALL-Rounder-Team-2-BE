package com.yapp.artie.domain.exhibition.domain.dto.exhibition;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Getter
@Schema(description = "전시 Response")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExhibitionDetailResponse {

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

  @Schema(description = "전시 링크")
  private String attachedLink;

  @Schema(description = "고정 여부")
  private boolean isPinned;

  @Builder
  public ExhibitionDetailResponse(@NonNull Long id, @NonNull String name,
      @NonNull LocalDate postDate,
      boolean isPublished, @NonNull Long categoryId, @NonNull String categoryName,
      String mainImage, String attachedLink, boolean isPinned) {
    this.id = id;
    this.name = name;
    this.postDate = postDate;
    this.isPublished = isPublished;
    this.categoryId = categoryId;
    this.categoryName = categoryName;
    this.mainImage = mainImage;
    this.attachedLink = attachedLink;
    this.isPinned = isPinned;
  }

  public static class HomePage extends PageImpl<ExhibitionDetailResponse> {

    public HomePage(List<ExhibitionDetailResponse> content,
        Pageable pageable, long total) {
      super(content, pageable, total);
    }
  }
}
