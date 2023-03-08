package com.yapp.artie.domain.archive.service;

import com.yapp.artie.domain.archive.domain.category.Category;
import com.yapp.artie.domain.archive.domain.exhibit.Exhibit;
import com.yapp.artie.domain.archive.domain.exhibit.PinType;
import com.yapp.artie.domain.archive.dto.exhibit.CalendarExhibitRequestDto;
import com.yapp.artie.domain.archive.dto.exhibit.CalendarExhibitResponseDto;
import com.yapp.artie.domain.archive.dto.exhibit.CalenderQueryResultDto;
import com.yapp.artie.domain.archive.dto.exhibit.CreateExhibitRequestDto;
import com.yapp.artie.domain.archive.dto.exhibit.ExhibitByDateResponseDto;
import com.yapp.artie.domain.archive.dto.exhibit.PostDetailInfo;
import com.yapp.artie.domain.archive.dto.exhibit.PostInfoByCategoryDto;
import com.yapp.artie.domain.archive.dto.exhibit.PostInfoDto;
import com.yapp.artie.domain.archive.dto.exhibit.UpdateExhibitRequestDto;
import com.yapp.artie.domain.archive.exception.ExhibitNotFoundException;
import com.yapp.artie.domain.archive.exception.NotOwnerOfExhibitException;
import com.yapp.artie.domain.archive.repository.ArtworkRepository;
import com.yapp.artie.domain.archive.repository.ExhibitRepository;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.service.UserService;
import com.yapp.artie.global.util.DateUtils;
import com.yapp.artie.global.util.S3Utils;
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
public class ExhibitService {

  private final ExhibitRepository exhibitRepository;
  private final ArtworkRepository artworkRepository;
  private final UserService userService;
  private final CategoryService categoryService;
  private final S3Utils s3Utils;

  public int getExhibitCount(Long userId) {
    return exhibitRepository.countExhibit(findUser(userId));
  }

  public PostInfoDto getExhibitInformation(Long id, Long userId) {
    Exhibit exhibit = exhibitRepository.findExhibitEntityGraphById(id)
        .orElseThrow(ExhibitNotFoundException::new);
    validateOwnedByUser(findUser(userId), exhibit);

    return buildExhibitionInformation(exhibit);
  }

  public PostDetailInfo getDetailExhibitInformation(Long id, Long userId) {
    Exhibit exhibit = exhibitRepository.findDetailExhibitEntityGraphById(id)
        .orElseThrow(ExhibitNotFoundException::new);
    validateOwnedByUser(findUser(userId), exhibit);

    return buildDetailExhibitionInformation(exhibit, getMainImageUri(exhibit));
  }

  public List<PostInfoDto> getDraftExhibits(Long userId) {
    return exhibitRepository.findDraftExhibitDto(findUser(userId));
  }

  public Page<PostDetailInfo> getExhibitByPage(Long categoryId, Long userId, int page, int size,
      Direction direction) {
    if (categoryId != null) {
      return getExhibitByCategoryAsPage(categoryId, userId, page, size, direction);
    }

    Pageable pageable = PageRequest.of(page, size, getSortCondition(PinType.ALL, direction));
    AtomicBoolean isFirstElement = new AtomicBoolean(page == 0);
    return exhibitRepository
        .findExhibitAsPage(pageable, findUser(userId))
        .map(exhibit -> buildPostDetailInfoForHome(exhibit, getMainImageUri(exhibit),
            isFirstElement.get() && isFirstElement.getAndSet(false), PinType.ALL));
  }

  private Page<PostDetailInfo> getExhibitByCategoryAsPage(Long categoryId, Long userId,
      int page, int size, Direction direction) {

    Category category = categoryService.findCategoryWithUser(categoryId, userId);
    Pageable pageable = PageRequest.of(page, size, getSortCondition(PinType.CATEGORY, direction));
    AtomicBoolean isFirstElement = new AtomicBoolean(page == 0);
    return exhibitRepository
        .findExhibitByCategoryAsPage(pageable, findUser(userId), category)
        .map(exhibit -> buildPostDetailInfoForHome(exhibit, getMainImageUri(exhibit),
            isFirstElement.get() && isFirstElement.getAndSet(false), PinType.CATEGORY));
  }

  public List<CalendarExhibitResponseDto> getExhibitByMonthly(
      CalendarExhibitRequestDto calendarExhibitRequestDto, Long userId) {
    int year = calendarExhibitRequestDto.getYear();
    int month = calendarExhibitRequestDto.getMonth();

    LocalDateTime start = DateUtils.getFirstDayOf(year, month);
    LocalDateTime end = DateUtils.getLastDayOf(year, month);

    return exhibitRepository.findExhibitAsCalenderByDay(start, end, userId)
        .stream()
        .map(this::buildCalendarExhibitInformation).collect(
            Collectors.toList());
  }

  public Exhibit getExhibitByUser(Long id, Long userId) {
    Exhibit exhibit = exhibitRepository.findExhibitEntityGraphById(id)
        .orElseThrow(ExhibitNotFoundException::new);
    validateOwnedByUser(findUser(userId), exhibit);

    return exhibit;
  }

  public Page<PostInfoByCategoryDto> getExhibitThumbnailByCategory(Long userId, Long categoryId,
      int page, int size) {

    Category category = categoryService.findCategoryWithUser(categoryId, userId);
    return exhibitRepository.findExhibitByCategoryAsPage(
            PageRequest.of(page, size, JpaSort.by(Direction.DESC, "createdAt")),
            findUser(userId), category)
        .map(this::buildPostInfoByCategoryDto);
  }

  public List<ExhibitByDateResponseDto> getExhibitsByDate(Long userId, int year, int month,
      int day) {

    LocalDateTime start = DateUtils.getStartTimeOf(year, month, day);
    LocalDateTime end = DateUtils.getEndTimeOf(year, month, day);
    return exhibitRepository.findExhibitsByDate(findUser(userId), start, end);
  }

  @Transactional
  public Long create(CreateExhibitRequestDto createExhibitRequestDto, Long userId) {
    Category category = categoryService.findCategoryWithUser(
        createExhibitRequestDto.getCategoryId(), userId);
    Exhibit exhibit = Exhibit.create(createExhibitRequestDto.getName(),
        createExhibitRequestDto.getPostDate(), category, findUser(userId),
        createExhibitRequestDto.getAttachedLink());

    return exhibitRepository.save(exhibit)
        .getId();
  }

  @Transactional
  public void publish(Long id, Long userId) {
    Exhibit exhibit = exhibitRepository.findExhibitEntityGraphById(id)
        .orElseThrow(ExhibitNotFoundException::new);
    validateOwnedByUser(findUser(userId), exhibit);

    exhibit.publish();
  }

  @Transactional
  public void update(UpdateExhibitRequestDto updateExhibitRequestDto, Long id, Long userId) {
    Exhibit exhibit = exhibitRepository.findExhibitEntityGraphById(id)
        .orElseThrow(ExhibitNotFoundException::new);
    validateOwnedByUser(findUser(userId), exhibit);
    Category category = categoryService.findCategoryWithUser(
        updateExhibitRequestDto.getCategoryId(), userId);

    exhibit.update(updateExhibitRequestDto.getName(), updateExhibitRequestDto.getPostDate(),
        updateExhibitRequestDto.getAttachedLink(), category);
  }

  @Transactional
  public void delete(Long id, Long userId) {
    Exhibit exhibit = exhibitRepository.findExhibitEntityGraphById(id)
        .orElseThrow(ExhibitNotFoundException::new);
    validateOwnedByUser(findUser(userId), exhibit);

    exhibitRepository.deleteById(id);
  }

  @Transactional
  public void updatePostPinType(Long userId, Long exhibitId, boolean categoryType, boolean pinned) {
    Exhibit exhibit = exhibitRepository.findDetailExhibitEntityGraphById(exhibitId)
        .orElseThrow(ExhibitNotFoundException::new);
    validateOwnedByUser(findUser(userId), exhibit);

    if (pinned) {
      setExhibitPin(categoryType, exhibit);
    } else {
      setExhibitNotPin(categoryType, exhibit);
    }
  }

  private User findUser(Long userId) {
    return userService.findById(userId);
  }

  private String getMainImageUri(Exhibit exhibit) {
    return artworkRepository.findMainArtworkByExhibitId(exhibit)
        .map(artwork -> s3Utils.getFullUri(artwork.getContents().getUri())).orElse(null);
  }

  // TODO : public이 아니도록 수정
  public void validateOwnedByUser(User user, Exhibit exhibit) {
    if (!exhibit.ownedBy(user)) {
      throw new NotOwnerOfExhibitException();
    }
  }

  private CalendarExhibitResponseDto buildCalendarExhibitInformation(
      CalenderQueryResultDto queryResult) {
    LocalDate date = LocalDate.parse(queryResult.getCalenderDate(), DateTimeFormatter.ISO_DATE);
    return new CalendarExhibitResponseDto(date, queryResult.getPostId(),
        s3Utils.getFullUri(queryResult.getUri()), queryResult.getPostNum());
  }

  private PostInfoDto buildExhibitionInformation(Exhibit exhibit) {
    return new PostInfoDto(exhibit.getId(), exhibit.contents().getName(),
        exhibit.contents().getDate(), exhibit.contents().getAttachedLink(), exhibit.isPublished());
  }

  private PostDetailInfo buildDetailExhibitionInformation(Exhibit exhibit, String imageUri) {
    return PostDetailInfo.builder()
        .id(exhibit.getId())
        .name(exhibit.contents().getName())
        .postDate(exhibit.contents().getDate())
        .isPublished(exhibit.isPublished())
        .categoryId(exhibit.getCategory().getId())
        .categoryName(exhibit.getCategory().getName())
        .mainImage(imageUri)
        .attachedLink(exhibit.contents().getAttachedLink())
        .isPinned(false)
        .build();
  }

  private PostDetailInfo buildPostDetailInfoForHome(Exhibit exhibit, String imageUri,
      boolean isFirstElement, PinType pagePinType) {
    return PostDetailInfo.builder()
        .id(exhibit.getId())
        .name(exhibit.contents().getName())
        .postDate(exhibit.contents().getDate())
        .isPublished(exhibit.isPublished())
        .categoryId(exhibit.getCategory().getId())
        .categoryName(exhibit.getCategory().getName())
        .mainImage(imageUri)
        .attachedLink(exhibit.contents().getAttachedLink())
        .isPinned(isFirstElement && getIsPinned(exhibit.getPinType(), pagePinType))
        .build();
  }

  private PostInfoByCategoryDto buildPostInfoByCategoryDto(Exhibit exhibit) {
    return new PostInfoByCategoryDto(exhibit.getId(), exhibit.contents().getName(),
        getMainImageUri(exhibit));
  }

  private void setExhibitPin(boolean categoryType, Exhibit exhibit) {
    PinType newPinType;
    if (categoryType) {
      Optional<Exhibit> pinnedExhibit = exhibitRepository.findPinnedExhibitWithCategory(
          exhibit.getCategory(),
          new PinType[]{PinType.BOTH, PinType.CATEGORY});
      pinnedExhibit.ifPresent(value -> value.updatePinType(value
          .getPinType() == PinType.BOTH ? PinType.ALL : PinType.NONE));

      newPinType = exhibit.getPinType() == PinType.ALL ? PinType.BOTH : PinType.CATEGORY;
    } else {
      Optional<Exhibit> pinnedExhibit = exhibitRepository.findPinnedExhibit(
          new PinType[]{PinType.BOTH, PinType.ALL});
      pinnedExhibit.ifPresent(value -> value.updatePinType(value
          .getPinType() == PinType.BOTH ? PinType.CATEGORY : PinType.NONE));

      newPinType = exhibit.getPinType() == PinType.CATEGORY ? PinType.BOTH : PinType.ALL;
    }
    exhibit.updatePinType(newPinType);
  }

  private void setExhibitNotPin(boolean categoryType, Exhibit exhibit) {
    PinType newPinType;
    if (categoryType) {
      newPinType = exhibit.getPinType() == PinType.BOTH ? PinType.ALL : PinType.NONE;
    } else {
      newPinType = exhibit.getPinType() == PinType.BOTH ? PinType.CATEGORY : PinType.NONE;
    }
    exhibit.updatePinType(newPinType);
  }

  private JpaSort getSortCondition(PinType pinType, Direction direction) {
    return JpaSort.unsafe(Direction.ASC,
            String.format("case when e.pinType in ('BOTH','%s') then 1 else 2 end", pinType))
        .andUnsafe(direction, "createdAt");
  }

  private boolean getIsPinned(PinType exhibitPinType, PinType pagePinType) {
    if (pagePinType == PinType.ALL && (exhibitPinType == PinType.BOTH
        || exhibitPinType == PinType.ALL)) {
      return true;
    } else {
      return pagePinType == PinType.CATEGORY && (exhibitPinType == PinType.BOTH
          || exhibitPinType == PinType.CATEGORY);
    }
  }
}
