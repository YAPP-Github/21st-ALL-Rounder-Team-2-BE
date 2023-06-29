package com.yapp.artie.gallery.service;

import com.yapp.artie.category.domain.Category;
import com.yapp.artie.category.service.CategoryService;
import com.yapp.artie.gallery.domain.entity.exhibition.Exhibition;
import com.yapp.artie.gallery.domain.entity.exhibition.PinType;
import com.yapp.artie.gallery.domain.repository.ArtworkRepository;
import com.yapp.artie.gallery.domain.repository.ExhibitionRepository;
import com.yapp.artie.gallery.dto.exhibition.CreateExhibitionRequest;
import com.yapp.artie.gallery.dto.exhibition.ExhibitionByCategoryResponse;
import com.yapp.artie.gallery.dto.exhibition.ExhibitionByDateResponse;
import com.yapp.artie.gallery.dto.exhibition.ExhibitionByDayQueryResponse;
import com.yapp.artie.gallery.dto.exhibition.ExhibitionByMonthlyResponse;
import com.yapp.artie.gallery.dto.exhibition.ExhibitionDetailResponse;
import com.yapp.artie.gallery.dto.exhibition.ExhibitionDraftResponse;
import com.yapp.artie.gallery.dto.exhibition.UpdateExhibitionRequest;
import com.yapp.artie.gallery.exception.ExhibitionNotFoundException;
import com.yapp.artie.gallery.exception.NotOwnerOfExhibitionException;
import com.yapp.artie.global.deprecated.LoadUserJpaEntityApi;
import com.yapp.artie.global.util.DateUtils;
import com.yapp.artie.global.util.S3Utils;
import com.yapp.artie.user.adapter.out.persistence.UserJpaEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExhibitionService {

  private final ExhibitionRepository exhibitionRepository;
  private final ArtworkRepository artworkRepository;
  private final LoadUserJpaEntityApi loadUserJpaEntityApi;
  private final CategoryService categoryService;
  private final S3Utils s3Utils;

  public int getExhibitionCount(Long userId) {
    return exhibitionRepository.countExhibition(findUser(userId));
  }

  public ExhibitionDetailResponse getExhibition(Long id, Long userId) {
    Exhibition exhibition = exhibitionRepository.findDetailExhibitionEntityGraphById(id)
        .orElseThrow(ExhibitionNotFoundException::new);
    validateOwnedByUser(findUser(userId), exhibition);

    return buildDetailExhibitionInformation(exhibition, getMainImageUri(exhibition));
  }

  public List<ExhibitionDraftResponse> getDraftExhibitions(Long userId) {
    return exhibitionRepository.findDraftExhibitions(findUser(userId));
  }

  public Page<ExhibitionDetailResponse> getExhibitionsForHomePage(Long categoryId, Long userId,
      int page,
      int size,
      Direction direction) {
    if (categoryId != null) {
      return getExhibitionsByCategoryAsPage(categoryId, userId, page, size, direction);
    }

    Pageable pageable = PageRequest.of(page, size, getSortCondition(PinType.ALL, direction));
    AtomicBoolean isFirstElement = new AtomicBoolean(page == 0);
    return exhibitionRepository
        .findExhibitionsAsPage(pageable, findUser(userId))
        .map(exhibition -> buildExhibitionDetailResponse(exhibition, getMainImageUri(exhibition),
            isFirstElement.get() && isFirstElement.getAndSet(false), PinType.ALL));
  }

  private Page<ExhibitionDetailResponse> getExhibitionsByCategoryAsPage(Long categoryId,
      Long userId,
      int page, int size, Direction direction) {

    Category category = categoryService.findCategoryWithUser(categoryId, userId);
    Pageable pageable = PageRequest.of(page, size, getSortCondition(PinType.CATEGORY, direction));
    AtomicBoolean isFirstElement = new AtomicBoolean(page == 0);
    return exhibitionRepository
        .findExhibitionsByCategoryAsPage(pageable, findUser(userId), category)
        .map(exhibition -> buildExhibitionDetailResponse(exhibition, getMainImageUri(exhibition),
            isFirstElement.get() && isFirstElement.getAndSet(false), PinType.CATEGORY));
  }

  public List<ExhibitionByMonthlyResponse> getExhibitionsByMonthly(
      int year, int month, Long userId) {

    LocalDateTime start = DateUtils.getFirstDayOf(year, month);
    LocalDateTime end = DateUtils.getLastDayOf(year, month);

    return exhibitionRepository.findExhibitionsByDay(start, end, userId)
        .stream()
        .map(this::buildExhibitionByMonthlyResponse)
        .collect(Collectors.toList());
  }

  public Exhibition getExhibitionByUser(Long id, Long userId) {
    Exhibition exhibition = exhibitionRepository.findExhibitionEntityGraphById(id)
        .orElseThrow(ExhibitionNotFoundException::new);
    validateOwnedByUser(findUser(userId), exhibition);

    return exhibition;
  }

  public Page<ExhibitionByCategoryResponse> getExhibitionsByCategory(Long userId, Long categoryId,
      int page, int size) {

    Category category = categoryService.findCategoryWithUser(categoryId, userId);
    return exhibitionRepository.findExhibitionsByCategoryAsPage(
            PageRequest.of(page, size, JpaSort.by(Direction.DESC, "createdAt")),
            findUser(userId), category)
        .map(this::buildExhibitionByCategoryResponse);
  }

  public List<ExhibitionByDateResponse> getExhibitionsByDate(Long userId, int year, int month,
      int day) {

    LocalDateTime start = DateUtils.getStartTimeOf(year, month, day);
    LocalDateTime end = DateUtils.getEndTimeOf(year, month, day);
    return exhibitionRepository.findExhibitionsByDate(findUser(userId), start, end);
  }

  @Transactional
  public Long create(CreateExhibitionRequest createExhibitionRequest, Long userId) {
    Category category = categoryService.findCategoryWithUser(
        createExhibitionRequest.getCategoryId(), userId);
    Exhibition exhibition = Exhibition.create(createExhibitionRequest.getName(),
        createExhibitionRequest.getPostDate(), category, findUser(userId),
        createExhibitionRequest.getAttachedLink());

    return exhibitionRepository.save(exhibition)
        .getId();
  }

  @Transactional
  public void publish(Long id, Long userId) {
    Exhibition exhibition = exhibitionRepository.findExhibitionEntityGraphById(id)
        .orElseThrow(ExhibitionNotFoundException::new);
    validateOwnedByUser(findUser(userId), exhibition);

    exhibition.publish();
  }

  @Transactional
  public void update(UpdateExhibitionRequest updateExhibitionRequest, Long id, Long userId) {
    Exhibition exhibition = exhibitionRepository.findExhibitionEntityGraphById(id)
        .orElseThrow(ExhibitionNotFoundException::new);
    validateOwnedByUser(findUser(userId), exhibition);

    String name = exhibition.contents().getName();
    LocalDate date = exhibition.contents().getDate();
    Category category = exhibition.getCategory();
    String attachedLink = exhibition.contents().getAttachedLink();

    if (updateExhibitionRequest.getName() != null) {
      name = updateExhibitionRequest.getName();
    }
    if (updateExhibitionRequest.getPostDate() != null) {
      date = updateExhibitionRequest.getPostDate();
    }
    if (updateExhibitionRequest.getCategoryId() != null) {
      category = categoryService.findCategoryWithUser(
          updateExhibitionRequest.getCategoryId(), userId);
    }
    if (updateExhibitionRequest.getAttachedLink() != null) {
      attachedLink = updateExhibitionRequest.getAttachedLink();
    }

    exhibition.update(name, date, attachedLink, category);
  }

  @Transactional
  public void delete(Long id, Long userId) {
    Exhibition exhibition = exhibitionRepository.findExhibitionEntityGraphById(id)
        .orElseThrow(ExhibitionNotFoundException::new);
    validateOwnedByUser(findUser(userId), exhibition);

    exhibitionRepository.deleteById(id);
  }

  @Transactional
  public void updateExhibitionPinType(Long userId, Long exhibitId, boolean categoryType,
      boolean pinned) {
    Exhibition exhibition = exhibitionRepository.findDetailExhibitionEntityGraphById(exhibitId)
        .orElseThrow(ExhibitionNotFoundException::new);
    validateOwnedByUser(findUser(userId), exhibition);

    if (pinned) {
      setExhibitionPinType(categoryType, exhibition);
    } else {
      setExhibitionNotPinned(categoryType, exhibition);
    }
  }

  private UserJpaEntity findUser(Long userId) {
    return loadUserJpaEntityApi.findById(userId);
  }

  private String getMainImageUri(Exhibition exhibition) {
    return artworkRepository.findMainArtworkByExhibitionId(exhibition)
        .map(artwork -> s3Utils.getFullUri(artwork.getContents().getUri())).orElse(null);
  }

  // TODO : public이 아니도록 수정
  public void validateOwnedByUser(UserJpaEntity user, Exhibition exhibition) {
    if (!exhibition.ownedBy(user)) {
      throw new NotOwnerOfExhibitionException();
    }
  }

  private ExhibitionByMonthlyResponse buildExhibitionByMonthlyResponse(
      ExhibitionByDayQueryResponse queryResult) {
    LocalDate date = LocalDate.parse(queryResult.getCalenderDate(), DateTimeFormatter.ISO_DATE);
    return new ExhibitionByMonthlyResponse(date, queryResult.getPostId(),
        s3Utils.getFullUri(queryResult.getUri()), queryResult.getPostNum());
  }

  private ExhibitionDetailResponse buildDetailExhibitionInformation(Exhibition exhibition,
      String imageUri) {
    return ExhibitionDetailResponse.builder()
        .id(exhibition.getId())
        .name(exhibition.contents().getName())
        .postDate(exhibition.contents().getDate())
        .isPublished(exhibition.isPublished())
        .categoryId(exhibition.getCategory().getId())
        .categoryName(exhibition.getCategory().getName())
        .mainImage(imageUri)
        .attachedLink(exhibition.contents().getAttachedLink())
        .isPinned(false)
        .build();
  }

  private ExhibitionDetailResponse buildExhibitionDetailResponse(Exhibition exhibition,
      String imageUri, boolean isFirstElement, PinType pagePinType) {
    return ExhibitionDetailResponse.builder()
        .id(exhibition.getId())
        .name(exhibition.contents().getName())
        .postDate(exhibition.contents().getDate())
        .isPublished(exhibition.isPublished())
        .categoryId(exhibition.getCategory().getId())
        .categoryName(exhibition.getCategory().getName())
        .mainImage(imageUri)
        .attachedLink(exhibition.contents().getAttachedLink())
        .isPinned(isFirstElement && getIsPinned(exhibition.getPinType(), pagePinType))
        .build();
  }

  private ExhibitionByCategoryResponse buildExhibitionByCategoryResponse(Exhibition exhibition) {
    return new ExhibitionByCategoryResponse(exhibition.getId(), exhibition.contents().getName(),
        getMainImageUri(exhibition));
  }

  private void setExhibitionPinType(boolean categoryType, Exhibition exhibition) {
    PinType newPinType;
    if (categoryType) {
      Optional<Exhibition> pinnedExhibition = exhibitionRepository.findPinnedExhibitionWithCategory(
          exhibition.getCategory(),
          new PinType[]{PinType.BOTH, PinType.CATEGORY});
      pinnedExhibition.ifPresent(value -> value.updatePinType(value
          .getPinType() == PinType.BOTH ? PinType.ALL : PinType.NONE));

      newPinType = exhibition.getPinType() == PinType.ALL ? PinType.BOTH : PinType.CATEGORY;
    } else {
      Optional<Exhibition> pinnedExhibition = exhibitionRepository.findPinnedExhibition(
          new PinType[]{PinType.BOTH, PinType.ALL});
      pinnedExhibition.ifPresent(value -> value.updatePinType(value
          .getPinType() == PinType.BOTH ? PinType.CATEGORY : PinType.NONE));

      newPinType = exhibition.getPinType() == PinType.CATEGORY ? PinType.BOTH : PinType.ALL;
    }
    exhibition.updatePinType(newPinType);
  }

  private void setExhibitionNotPinned(boolean categoryType, Exhibition exhibition) {
    PinType newPinType;
    if (categoryType) {
      newPinType = exhibition.getPinType() == PinType.BOTH ? PinType.ALL : PinType.NONE;
    } else {
      newPinType = exhibition.getPinType() == PinType.BOTH ? PinType.CATEGORY : PinType.NONE;
    }
    exhibition.updatePinType(newPinType);
  }

  private JpaSort getSortCondition(PinType pinType, Direction direction) {
    return JpaSort.unsafe(Direction.ASC,
            String.format("case when e.pinType in ('BOTH','%s') then 1 else 2 end", pinType))
        .andUnsafe(direction, "createdAt");
  }

  private boolean getIsPinned(PinType exhibitionPinType, PinType pagePinType) {
    if (pagePinType == PinType.ALL && (exhibitionPinType == PinType.BOTH
        || exhibitionPinType == PinType.ALL)) {
      return true;
    } else {
      return pagePinType == PinType.CATEGORY && (exhibitionPinType == PinType.BOTH
          || exhibitionPinType == PinType.CATEGORY);
    }
  }
}
