package com.yapp.artie.domain.exhibition.domain.dto.exhibition;

import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class PostInfoByCategoryDtoPage extends PageImpl<ExhibitionByCategoryResponse> {

  public PostInfoByCategoryDtoPage(List<ExhibitionByCategoryResponse> content,
      Pageable pageable, long total) {
    super(content, pageable, total);
  }
}
