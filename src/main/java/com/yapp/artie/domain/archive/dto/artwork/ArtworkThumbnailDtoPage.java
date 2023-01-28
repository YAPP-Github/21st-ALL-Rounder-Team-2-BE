package com.yapp.artie.domain.archive.dto.artwork;

import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class ArtworkThumbnailDtoPage extends PageImpl<ArtworkThumbnailDto> {

  public ArtworkThumbnailDtoPage(List<ArtworkThumbnailDto> content,
      Pageable pageable, long total) {
    super(content, pageable, total);
  }
}
