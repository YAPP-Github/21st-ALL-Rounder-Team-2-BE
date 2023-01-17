package com.yapp.artie.domain.archive.service;

import com.yapp.artie.domain.archive.domain.artwork.Artwork;
import com.yapp.artie.domain.archive.domain.exhibit.Exhibit;
import com.yapp.artie.domain.archive.dto.artwork.CreateArtworkRequestDto;
import com.yapp.artie.domain.archive.exception.NotOwnerOfExhibitException;
import com.yapp.artie.domain.archive.repository.ArtworkRepository;
import com.yapp.artie.domain.archive.repository.ExhibitRepository;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.exception.UserNotFoundException;
import com.yapp.artie.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtworkService {

  private final UserService userService;
  private final ExhibitRepository exhibitRepository;
  private final ArtworkRepository artworkRepository;
  private final TagService tagService;

  @Transactional
  public Long create(CreateArtworkRequestDto createArtworkRequestDto, Long userId) {
    User user = findUser(userId);
    Exhibit exhibit = exhibitRepository.findExhibitEntityGraphById(
        createArtworkRequestDto.getPostId()).orElseThrow(UserNotFoundException::new);
    if (!exhibit.ownedBy(user)) {
      throw new NotOwnerOfExhibitException();
    }

    Long artworkNum = artworkRepository.countArtworkByExhibitId(exhibit.getId());
    boolean isMain = artworkNum <= 0;

    Artwork artwork = artworkRepository.save(
        Artwork.create(exhibit, isMain, createArtworkRequestDto.getName(),
            createArtworkRequestDto.getArtist(), createArtworkRequestDto.getImageUri()));

    tagService.addTagsToArtwork(createArtworkRequestDto.getTags(), artwork, user);

    return artwork.getId();
  }

  private User findUser(Long userId) {
    return userService.findById(userId)
        .orElseThrow(UserNotFoundException::new);
  }
}
