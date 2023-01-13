package com.yapp.artie.domain.archive.service;

import com.yapp.artie.domain.archive.domain.tag.ArtworkTag;
import com.yapp.artie.domain.archive.repository.ArtworkTagRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtworkTagService {

  private final ArtworkTagRepository artworkTagRepository;

  @Transactional
  public int createAll(List<ArtworkTag> artworkTagDtos) {
    return artworkTagRepository.saveAll(artworkTagDtos).size();
  }
}
