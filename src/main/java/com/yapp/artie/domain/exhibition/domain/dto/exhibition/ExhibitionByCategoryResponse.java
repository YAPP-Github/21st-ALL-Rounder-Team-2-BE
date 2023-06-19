package com.yapp.artie.domain.exhibition.domain.dto.exhibition;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


@Getter
@Schema(description = "카테고리 페이지 내 전시 목록 썸네일")
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
