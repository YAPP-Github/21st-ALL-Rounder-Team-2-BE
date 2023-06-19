package com.yapp.artie.domain.exhibition.domain.dto.artwork;

import java.util.List;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class ArtworkThumbnailAsPage extends PageImpl<ArtworkThumbnailResponse> {

  public ArtworkThumbnailAsPage(List<ArtworkThumbnailResponse> content,
      Pageable pageable, long total) {
    super(content, pageable, total);
  }
}
