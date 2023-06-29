package com.yapp.artie.gallery.domain.service;

import com.yapp.artie.gallery.domain.dto.artwork.ArtworkDetailResponse;
import com.yapp.artie.gallery.domain.dto.artwork.ArtworkImageThumbnailResponse;
import com.yapp.artie.gallery.domain.dto.artwork.ArtworkThumbnailResponse;
import com.yapp.artie.gallery.domain.dto.artwork.CreateArtworkRequest;
import com.yapp.artie.gallery.domain.dto.artwork.UpdateArtworkRequest;
import com.yapp.artie.gallery.domain.entity.artwork.Artwork;
import com.yapp.artie.gallery.domain.entity.exhibition.Exhibition;
import com.yapp.artie.gallery.domain.repository.ArtworkRepository;
import com.yapp.artie.gallery.exception.ArtworkNotFoundException;
import com.yapp.artie.global.deprecated.LoadUserJpaEntityApi;
import com.yapp.artie.global.util.S3Utils;
import com.yapp.artie.s3.service.S3Service;
import com.yapp.artie.user.adapter.out.persistence.UserJpaEntity;
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
  private final ExhibitionService exhibitionService;
  private final S3Service s3Service;
  private final ArtworkRepository artworkRepository;
  private final S3Utils s3Utils;

  public Page<ArtworkThumbnailResponse> getArtworkThumbnails(Long exhibitId, Long userId, int page,
      int size,
      Direction direction) {
    Exhibition exhibition = exhibitionService.getExhibitionByUser(exhibitId, userId);
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt", "id"));
    return artworkRepository.findAllArtworkAsPage(pageable, exhibition)
        .map(this::buildArtworkThumbnail);
  }

  public ArtworkDetailResponse getArtworkDetail(Long artworkId, Long userId) {
    Artwork artwork = findById(artworkId, userId);
    List<String> tags = tagService.getTagNames(artwork);
    return buildArtworkInfo(artwork, tags);
  }

  public List<ArtworkImageThumbnailResponse> getArtworkImageThumbnails(Long exhibitId,
      Long userId) {
    Exhibition exhibition = exhibitionService.getExhibitionByUser(exhibitId, userId);
    return artworkRepository.findArtworksByExhibitionOrderByCreatedAtDescIdDesc(exhibition).stream()
        .map(this::buildArtworkBrowseThumbnail).collect(Collectors.toList());
  }

  @Transactional
  public Long create(CreateArtworkRequest createArtworkRequest, Long userId) {
    Exhibition exhibition = exhibitionService.getExhibitionByUser(createArtworkRequest.getPostId(),
        userId);

    Long artworkNum = artworkRepository.countArtworkByExhibitionId(exhibition.getId());

    Artwork artwork = artworkRepository.save(
        Artwork.create(exhibition, artworkNum <= 0, createArtworkRequest.getName(),
            createArtworkRequest.getArtist(), createArtworkRequest.getImageUri()));

    UserJpaEntity user = loadUserJpaEntityApi.findById(userId);
    if (createArtworkRequest.getTags() != null) {
      tagService.addTagsToArtwork(createArtworkRequest.getTags(), artwork, user);
    }

    if (artworkNum == 0) {
      exhibition.publish();
    }

    return artwork.getId();
  }

  @Transactional
  public List<Long> createBatch(List<String> imageUriList, Long exhibitId, Long userId) {
    Exhibition exhibition = exhibitionService.getExhibitionByUser(exhibitId, userId);
    boolean emptyArtwork = artworkRepository.countArtworkByExhibitionId(exhibition.getId()) <= 0;

    List<Artwork> artworks = IntStream.range(0, imageUriList.size())
        .mapToObj(i -> Artwork.create(exhibition, i == 0 && emptyArtwork, imageUriList.get(i)))
        .collect(
            Collectors.toList());
    if (emptyArtwork) {
      exhibition.publish();
    }
    return artworkRepository.saveAll(artworks).stream().map(Artwork::getId)
        .collect(Collectors.toList());
  }

  @Transactional
  public void delete(Long id, Long userId) {
    Artwork artwork = findById(id, userId);
    String imageUri = artwork.getContents().getUri();

    Long artworkNum = artworkRepository.countArtworkByExhibitionId(artwork.getExhibition().getId());
    if (artworkNum <= 1) {
      exhibitionService.delete(artwork.getExhibition().getId(), userId);
    } else {
      artworkRepository.delete(artwork);
      if (artwork.isMain()) {
        artworkRepository.findFirstByExhibitionWithoutDeleted(
            artwork.getExhibition(), artwork.getId()).get(0).setMainArtwork();
      }
    }
    s3Service.deleteObject(imageUri);
  }

  @Transactional
  public void update(Long artworkId, Long userId, UpdateArtworkRequest updateArtworkRequest) {
    Artwork artwork = findById(artworkId, userId);
    if (StringUtils.isNotBlank(updateArtworkRequest.getArtist())) {
      artwork.getContents().updateArtist(updateArtworkRequest.getArtist());
    }
    if (StringUtils.isNotBlank(updateArtworkRequest.getName())) {
      artwork.getContents().updateName(updateArtworkRequest.getName());
    }
    if (updateArtworkRequest.getTags() != null) {
      tagService.deleteAllByArtwork(artwork);
      tagService.addTagsToArtwork(updateArtworkRequest.getTags(), artwork,
          loadUserJpaEntityApi.findById(userId));
    }
  }

  @Transactional
  public void setMainArtwork(Long artworkId, Long userId) {
    Artwork artwork = findById(artworkId, userId);
    if (!artwork.isMain()) {
      artworkRepository.updateArtworkNotMainByExhibition(artwork.getExhibition());
      artwork.setMainArtwork();
    }
  }

  private ArtworkThumbnailResponse buildArtworkThumbnail(Artwork artwork) {
    return ArtworkThumbnailResponse.builder()
        .id(artwork.getId())
        .imageURL(s3Utils.getFullUri(artwork.getContents().getUri()))
        .name(artwork.getContents().getName())
        .artist(artwork.getContents().getArtist())
        .build();
  }

  private ArtworkDetailResponse buildArtworkInfo(Artwork artwork, List<String> tags) {
    return ArtworkDetailResponse.builder()
        .id(artwork.getId())
        .imageURL(s3Utils.getFullUri(artwork.getContents().getUri()))
        .name(artwork.getContents().getName())
        .artist(artwork.getContents().getArtist())
        .tags(tags)
        .build();
  }

  private ArtworkImageThumbnailResponse buildArtworkBrowseThumbnail(Artwork artwork) {
    return new ArtworkImageThumbnailResponse(artwork.getId(),
        s3Utils.getFullUri(artwork.getContents().getUri()));
  }

  private Artwork findById(Long id, Long userId) {
    Artwork artwork = artworkRepository.findById(id).orElseThrow(ArtworkNotFoundException::new);
    exhibitionService.validateOwnedByUser(loadUserJpaEntityApi.findById(userId),
        artwork.getExhibition());
    return artwork;
  }
}
