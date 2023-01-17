package com.yapp.artie.domain.archive.service;

import com.yapp.artie.domain.archive.domain.artwork.Artwork;
import com.yapp.artie.domain.archive.domain.tag.ArtworkTag;
import com.yapp.artie.domain.archive.domain.tag.Tag;
import com.yapp.artie.domain.archive.dto.tag.CreateArtworkTagDto;
import com.yapp.artie.domain.archive.dto.tag.TagDto;
import com.yapp.artie.domain.archive.exception.TagAlreadyExistException;
import com.yapp.artie.domain.archive.exception.TagNotFoundException;
import com.yapp.artie.domain.archive.repository.TagRepository;
import com.yapp.artie.domain.user.domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService {

  private final TagRepository tagRepository;
  private final ArtworkTagService artworkTagService;

  @Transactional
  public int addTagsToArtwork(List<CreateArtworkTagDto> createArtworkTagDtoList, Artwork artwork,
      User user) {
    List<ArtworkTag> artworkTags = new ArrayList<>();
    AtomicInteger seq = new AtomicInteger(1);
    createArtworkTagDtoList.forEach(tagInfo -> {
      Tag tag;
      if (tagInfo.getTagId() != null) {
        tag = tagRepository.findById(tagInfo.getTagId())
            .orElseThrow(() -> new TagNotFoundException());
      } else {
        tag = tagRepository.save(this.create(tagInfo.getTagName(), user));
      }
      artworkTags.add(
          new ArtworkTag(tag, artwork, seq.getAndIncrement()));
    });
    return artworkTagService.createAll(artworkTags);
  }

  @Transactional
  public Tag create(String tagName, User user) {
    validateDuplicateTagName(tagName, user);

    Tag tag = Tag.create(user, tagName);
    tagRepository.save(tag);
    return tag;
  }


  private void validateDuplicateTagName(String tagName, User user) {
    List<TagDto> tags = tagRepository.findTagDto(user);
    long count = tags.stream().filter(tagDto -> tagDto.getName().equals(tagName)).count();
    if (count != 0) {
      throw new TagAlreadyExistException();
    }
  }

}
