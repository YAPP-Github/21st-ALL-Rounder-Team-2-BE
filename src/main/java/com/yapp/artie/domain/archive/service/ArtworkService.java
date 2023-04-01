package com.yapp.artie.domain.archive.service;

import com.yapp.artie.domain.archive.domain.artwork.Artwork;
import com.yapp.artie.domain.archive.domain.exhibit.Exhibit;
import com.yapp.artie.domain.archive.dto.artwork.ArtworkBrowseThumbnailDto;
import com.yapp.artie.domain.archive.dto.artwork.ArtworkInfoDto;
import com.yapp.artie.domain.archive.dto.artwork.ArtworkThumbnailDto;
import com.yapp.artie.domain.archive.dto.artwork.CreateArtworkRequestDto;
import com.yapp.artie.domain.archive.dto.artwork.UpdateArtworkRequestDto;
import com.yapp.artie.domain.archive.exception.ArtworkNotFoundException;
import com.yapp.artie.domain.archive.repository.ArtworkRepository;
import com.yapp.artie.domain.s3.service.S3Service;
import com.yapp.artie.domain.user.adapter.out.persistence.UserJpaEntity;
import com.yapp.artie.domain.user.deprecated.LoadUserJpaEntityApi;
import com.yapp.artie.global.util.S3Utils;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ArtworkService {

  private final LoadUserJpaEntityApi loadUserJpaEntityApi;
  private final TagService tagService;
  private final ExhibitService exhibitService;
  private final S3Service s3Service;
  private final ArtworkRepository artworkRepository;
  private final S3Utils s3Utils;

  public Page<ArtworkThumbnailDto> getArtworkAsPage(Long exhibitId, Long userId, int page, int size,
      Direction direction) {
    Exhibit exhibit = exhibitService.getExhibitByUser(exhibitId, userId);
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt", "id"));
    return artworkRepository.findAllArtworkAsPage(pageable, exhibit)
        .map(this::buildArtworkThumbnail);
  }

  public ArtworkInfoDto getArtworkInfo(Long artworkId, Long userId) {
    Artwork artwork = findById(artworkId, userId);
    List<String> tags = tagService.getTagNames(artwork);
    return buildArtworkInfo(artwork, tags);
  }

  public List<ArtworkBrowseThumbnailDto> getArtworkBrowseThumbnail(Long exhibitId, Long userId) {
    Exhibit exhibit = exhibitService.getExhibitByUser(exhibitId, userId);
    return artworkRepository.findArtworksByExhibitOrderByCreatedAtDescIdDesc(exhibit).stream()
        .map(this::buildArtworkBrowseThumbnail).collect(Collectors.toList());
  }

  @Transactional
  public Long create(CreateArtworkRequestDto createArtworkRequestDto, Long userId) {
    Exhibit exhibit = exhibitService.getExhibitByUser(createArtworkRequestDto.getPostId(), userId);

    Long artworkNum = artworkRepository.countArtworkByExhibitId(exhibit.getId());

    Artwork artwork = artworkRepository.save(
        Artwork.create(exhibit, artworkNum <= 0, createArtworkRequestDto.getName(),
            createArtworkRequestDto.getArtist(), createArtworkRequestDto.getImageUri()));

    UserJpaEntity user = loadUserJpaEntityApi.findById(userId);
    if (createArtworkRequestDto.getTags() != null) {
      tagService.addTagsToArtwork(createArtworkRequestDto.getTags(), artwork, user);
    }

    if (artworkNum == 0) {
      exhibit.publish();
    }

    return artwork.getId();
  }

  @Transactional
  public List<Long> createBatch(List<String> imageUriList, Long exhibitId, Long userId) {
    Exhibit exhibit = exhibitService.getExhibitByUser(exhibitId, userId);
    boolean emptyArtwork = artworkRepository.countArtworkByExhibitId(exhibit.getId()) <= 0;

    List<Artwork> artworks = IntStream.range(0, imageUriList.size())
        .mapToObj(i -> Artwork.create(exhibit, i == 0 && emptyArtwork, imageUriList.get(i)))
        .collect(
            Collectors.toList());
    if (emptyArtwork) {
      exhibit.publish();
    }
    return artworkRepository.saveAll(artworks).stream().map(Artwork::getId)
        .collect(Collectors.toList());
  }

  @Transactional
  public void delete(Long id, Long userId) {
    Artwork artwork = findById(id, userId);
    String imageUri = artwork.getContents().getUri();

    Long artworkNum = artworkRepository.countArtworkByExhibitId(artwork.getExhibit().getId());
    if (artworkNum <= 1) {
      exhibitService.delete(artwork.getExhibit().getId(), userId);
    } else {
      artworkRepository.delete(artwork);
      if (artwork.isMain()) {
        artworkRepository.findFirstByExhibitWithoutDeleted(
            artwork.getExhibit(), artwork.getId()).get(0).setMainArtwork();
      }
    }
    s3Service.deleteObject(imageUri);
  }

  @Transactional
  public void update(Long artworkId, Long userId, UpdateArtworkRequestDto updateArtworkRequestDto) {
    Artwork artwork = findById(artworkId, userId);
    if (StringUtils.isNotBlank(updateArtworkRequestDto.getArtist())) {
      artwork.getContents().updateArtist(updateArtworkRequestDto.getArtist());
    }
    if (StringUtils.isNotBlank(updateArtworkRequestDto.getName())) {
      artwork.getContents().updateName(updateArtworkRequestDto.getName());
    }
    if (updateArtworkRequestDto.getTags() != null) {
      tagService.deleteAllByArtwork(artwork);
      tagService.addTagsToArtwork(updateArtworkRequestDto.getTags(), artwork,
          loadUserJpaEntityApi.findById(userId));
    }
  }

  @Transactional
  public void setMainArtwork(Long artworkId, Long userId) {
    Artwork artwork = findById(artworkId, userId);
    artworkRepository.updateArtworkNotMainByExhibit(artwork.getExhibit());
    artwork.setMainArtwork();
  }

  private ArtworkThumbnailDto buildArtworkThumbnail(Artwork artwork) {
    return ArtworkThumbnailDto.builder()
        .id(artwork.getId())
        .imageURL(s3Utils.getFullUri(artwork.getContents().getUri()))
        .name(artwork.getContents().getName())
        .artist(artwork.getContents().getArtist())
        .build();
  }

  private ArtworkInfoDto buildArtworkInfo(Artwork artwork, List<String> tags) {
    return ArtworkInfoDto.builder()
        .id(artwork.getId())
        .imageURL(s3Utils.getFullUri(artwork.getContents().getUri()))
        .name(artwork.getContents().getName())
        .artist(artwork.getContents().getArtist())
        .tags(tags)
        .build();
  }

  private ArtworkBrowseThumbnailDto buildArtworkBrowseThumbnail(Artwork artwork) {
    return new ArtworkBrowseThumbnailDto(artwork.getId(),
        s3Utils.getFullUri(artwork.getContents().getUri()));
  }

  private Artwork findById(Long id, Long userId) {
    Artwork artwork = artworkRepository.findById(id).orElseThrow(ArtworkNotFoundException::new);
    exhibitService.validateOwnedByUser(loadUserJpaEntityApi.findById(userId), artwork.getExhibit());
    return artwork;
  }
}
