package com.yapp.artie.gallery.service;

import com.yapp.artie.gallery.domain.entity.artwork.Artwork;
import com.yapp.artie.gallery.domain.entity.artwork.Tag;
import com.yapp.artie.gallery.domain.repository.TagRepository;
import com.yapp.artie.user.adapter.out.persistence.UserJpaEntity;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService {

  private final TagRepository tagRepository;

  public List<String> getTagNames(Artwork artwork) {
    return tagRepository.findAllByArtworkOrderBySequenceAsc(artwork).stream()
        .map(Tag::getName).collect(Collectors.toList());
  }

  @Transactional
  public void addTagsToArtwork(List<String> tagNames, Artwork artwork,
      UserJpaEntity user) {
    AtomicInteger seq = new AtomicInteger(1);
    List<Tag> tags = tagNames.stream()
        .map(tagName -> new Tag(user, artwork, seq.getAndIncrement(), tagName))
        .collect(Collectors.toList());
    tagRepository.saveAll(tags);
  }

  @Transactional
  public void deleteAllByArtwork(Artwork artwork) {
    tagRepository.deleteAll(
        tagRepository.findAllByArtwork(artwork)
    );
  }
}
