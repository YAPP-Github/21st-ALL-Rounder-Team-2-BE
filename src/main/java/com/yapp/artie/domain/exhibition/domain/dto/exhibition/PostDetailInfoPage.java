package com.yapp.artie.domain.exhibition.domain.dto.exhibition;

import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class PostDetailInfoPage extends PageImpl<ExhibitionDetailResponse> {

  public PostDetailInfoPage(List<ExhibitionDetailResponse> content,
      Pageable pageable, long total) {
    super(content, pageable, total);
  }
}
