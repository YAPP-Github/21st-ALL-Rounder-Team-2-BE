package com.yapp.artie.gallery.dto.exhibition;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


@Getter
@Schema(description = "카테고리별 전시 정보 Response")
@RequiredArgsConstructor
public class ExhibitionByCategoryResponse {

  @NonNull
  @Schema(description = "전시 아이디")
  private Long id;

  @NonNull
  @Schema(description = "전시명")
  private String name;

  @NonNull
  @Schema(description = "대표 이미지")
  private String mainImage;

  public static class Page extends PageImpl<ExhibitionByCategoryResponse> {

    public Page(List<ExhibitionByCategoryResponse> content,
        Pageable pageable, long total) {
      super(content, pageable, total);
    }
  }
}
