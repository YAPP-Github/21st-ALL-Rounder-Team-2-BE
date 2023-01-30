package com.yapp.artie.domain.archive.service;

import com.yapp.artie.domain.archive.domain.artwork.Artwork;
import com.yapp.artie.domain.archive.domain.exhibit.Exhibit;
import com.yapp.artie.domain.archive.dto.artwork.ArtworkBrowseThumbnailDto;
import com.yapp.artie.domain.archive.dto.artwork.ArtworkInfoDto;
import com.yapp.artie.domain.archive.dto.artwork.ArtworkThumbnailDto;
import com.yapp.artie.domain.archive.dto.artwork.CreateArtworkRequestDto;
import com.yapp.artie.domain.archive.dto.tag.TagDto;
import com.yapp.artie.domain.archive.exception.ArtworkNotFoundException;
import com.yapp.artie.domain.archive.exception.NotOwnerOfExhibitException;
import com.yapp.artie.domain.archive.repository.ArtworkRepository;
import com.yapp.artie.domain.archive.repository.ExhibitRepository;
import com.yapp.artie.domain.s3.service.S3Service;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.exception.UserNotFoundException;
import com.yapp.artie.domain.user.service.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtworkService {

  @Value("${cloud.aws.cloudfront.domain}")
  private String cdnDomain;

  private final UserService userService;
  private final TagService tagService;
  private final ExhibitService exhibitService;
  private final ArtworkTagService artworkTagService;
  private final S3Service s3Service;
  private final ExhibitRepository exhibitRepository;
  private final ArtworkRepository artworkRepository;

  @Transactional
  public Long create(CreateArtworkRequestDto createArtworkRequestDto, Long userId) {
    User user = userService.findById(userId);
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

  public Page<ArtworkThumbnailDto> getArtworkAsPage(Long exhibitId, Long userId,
      Pageable pageable) {
    Exhibit exhibit = exhibitService.getExhibitByUser(exhibitId, userId);
    return artworkRepository.findAllArtworkAsPage(pageable, exhibit)
        .map(this::buildArtworkThumbnail);
  }

  public ArtworkInfoDto getArtworkInfo(Long artworkId, Long userId) {
    Artwork artwork = findById(artworkId, userId);
    List<TagDto> tags = artworkTagService.getTagDtosFromArtwork(artwork);
    return buildArtworkInfo(artwork, tags);
  }

  public List<ArtworkBrowseThumbnailDto> getArtworkBrowseThumbnail(Long exhibitId, Long userId) {
    Exhibit exhibit = exhibitService.getExhibitByUser(exhibitId, userId);
    return artworkRepository.findArtworksByExhibitOrderByCreatedAtDesc(exhibit).stream()
        .map(this::buildArtworkBrowseThumbnail).collect(Collectors.toList());
  }

  @Transactional
  public void delete(Long id, Long userId) {
    Artwork artwork = findById(id, userId);
    artworkTagService.deleteAllByArtwork(artwork);
    String imageUri = artwork.getContents().getUri();
    artworkRepository.delete(artwork);
    s3Service.deleteObject(imageUri);
  }

  private ArtworkThumbnailDto buildArtworkThumbnail(Artwork artwork) {
    return ArtworkThumbnailDto.builder()
        .id(artwork.getId())
        .imageURL(cdnDomain + artwork.getContents().getUri())
        .name(artwork.getContents().getName())
        .artist(artwork.getContents().getArtist())
        .build();
  }

  private ArtworkInfoDto buildArtworkInfo(Artwork artwork, List<TagDto> tags) {
    return ArtworkInfoDto.builder()
        .id(artwork.getId())
        .imageURL(cdnDomain + artwork.getContents().getUri())
        .name(artwork.getContents().getName())
        .artist(artwork.getContents().getArtist())
        .tags(tags)
        .build();
  }

  private ArtworkBrowseThumbnailDto buildArtworkBrowseThumbnail(Artwork artwork) {
    return new ArtworkBrowseThumbnailDto(artwork.getId(),
        cdnDomain + artwork.getContents().getUri());
  }

  private Artwork findById(Long id, Long userId) {
    Artwork artwork = artworkRepository.findById(id).orElseThrow(ArtworkNotFoundException::new);
    exhibitService.validateOwnedByUser(userService.findById(userId), artwork.getExhibit());
    return artwork;
  }
}
