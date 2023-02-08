package com.yapp.artie.domain.archive.dto.exhibit;

import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class PostInfoByCategoryDtoPage extends PageImpl<PostInfoByCategoryDto> {

  public PostInfoByCategoryDtoPage(List<PostInfoByCategoryDto> content,
      Pageable pageable, long total) {
    super(content, pageable, total);
  }
}
