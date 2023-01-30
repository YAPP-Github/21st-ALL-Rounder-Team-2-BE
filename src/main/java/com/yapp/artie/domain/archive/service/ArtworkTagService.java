package com.yapp.artie.domain.archive.service;

import com.yapp.artie.domain.archive.domain.artwork.Artwork;
import com.yapp.artie.domain.archive.domain.tag.ArtworkTag;
import com.yapp.artie.domain.archive.dto.tag.TagDto;
import com.yapp.artie.domain.archive.repository.ArtworkTagRepository;
import java.util.List;
import java.util.stream.Collectors;
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

  public List<TagDto> getTagDtosFromArtwork(Artwork artwork) {
    return artworkTagRepository.findArtworkTagsByArtworkOrderBySequenceAsc(
        artwork).stream().map(this::buildTagDto).collect(Collectors.toList());
  }

  @Transactional
  public void deleteAllByArtwork(Artwork artwork) {
    artworkTagRepository.deleteAll(
        artworkTagRepository.findArtworkTagsByArtwork(artwork)
    );
  }

  private TagDto buildTagDto(ArtworkTag artworkTag) {
    return new TagDto(artworkTag.getTag().getId(), artworkTag.getTag().getName());
  }

}
