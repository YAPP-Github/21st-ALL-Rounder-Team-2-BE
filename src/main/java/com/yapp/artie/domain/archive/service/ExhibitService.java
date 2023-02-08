package com.yapp.artie.domain.archive.service;

import com.yapp.artie.domain.archive.domain.category.Category;
import com.yapp.artie.domain.archive.domain.exhibit.Exhibit;
import com.yapp.artie.domain.archive.domain.exhibit.PinType;
import com.yapp.artie.domain.archive.dto.exhibit.CalendarExhibitRequestDto;
import com.yapp.artie.domain.archive.dto.exhibit.CalendarExhibitResponseDto;
import com.yapp.artie.domain.archive.dto.exhibit.CalenderQueryResultDto;
import com.yapp.artie.domain.archive.dto.exhibit.CreateExhibitRequestDto;
import com.yapp.artie.domain.archive.dto.exhibit.PostDetailInfo;
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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

  public Page<PostDetailInfo> getExhibitByPage(Long categoryId, Long userId, Pageable pageable,
      Direction direction) {
    if (categoryId != null) {
      return getExhibitByCategoryAsPage(categoryId, userId, pageable, direction);
    }

    JpaSort sort = JpaSort.unsafe(Direction.ASC,
            "case when e.pinType in ('BOTH','HOME') then 1 else 2 end")
        .andUnsafe(direction, "createdAt");
    return exhibitRepository.findExhibitAsPage(pageable, findUser(userId), sort)
        .map(exhibit -> buildDetailExhibitionInformation(exhibit, getMainImageUri(exhibit)));
  }

  public Page<PostDetailInfo> getExhibitByCategoryAsPage(Long categoryId, Long userId,
      Pageable pageable,
      Direction direction) {

    Category category = categoryService.findCategoryWithUser(categoryId, userId);
    JpaSort sort = JpaSort.unsafe(Direction.ASC,
            "case when e.pinType in ('BOTH','CATEGORY') then 1 else 2 end")
        .andUnsafe(direction, "createdAt");
    return exhibitRepository.findExhibitByCategoryAsPage(pageable, findUser(userId), category, sort)
        .map(exhibit -> buildDetailExhibitionInformation(exhibit, getMainImageUri(exhibit)));
  }

  public List<CalendarExhibitResponseDto> getExhibitByMonthly(
      CalendarExhibitRequestDto calendarExhibitRequestDto, Long userId) {
    int year = calendarExhibitRequestDto.getYear();
    int month = calendarExhibitRequestDto.getMonth();
    return exhibitRepository.findExhibitAsCalenderByDay(
            DateUtils.getFirstDayOf(year, month),
            DateUtils.getLastDayOf(year, month), userId).stream()
        .map(this::buildCalendarExhibitInformation).collect(
            Collectors.toList());
  }

  @Transactional
  public Long create(CreateExhibitRequestDto createExhibitRequestDto, Long userId) {
    Category category = categoryService.findCategoryWithUser(
        createExhibitRequestDto.getCategoryId(), userId);
    Exhibit exhibit = Exhibit.create(createExhibitRequestDto.getName(),
        createExhibitRequestDto.getPostDate(), category, findUser(userId));

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
        category);
  }

  @Transactional
  public void delete(Long id, Long userId) {
    Exhibit exhibit = exhibitRepository.findExhibitEntityGraphById(id)
        .orElseThrow(ExhibitNotFoundException::new);
    validateOwnedByUser(findUser(userId), exhibit);

    exhibitRepository.deleteById(id);
  }

  public Exhibit getExhibitByUser(Long id, Long userId) {
    Exhibit exhibit = exhibitRepository.findExhibitEntityGraphById(id)
        .orElseThrow(ExhibitNotFoundException::new);
    validateOwnedByUser(findUser(userId), exhibit);

    return exhibit;
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
    LocalDate date = LocalDate.parse(queryResult.getDate(), DateTimeFormatter.ISO_DATE);
    return new CalendarExhibitResponseDto(date.getYear(),
        date.getMonthValue(), date.getDayOfMonth(),
        s3Utils.getFullUri(queryResult.getUri()));
  }

  private PostInfoDto buildExhibitionInformation(Exhibit exhibit) {
    return new PostInfoDto(exhibit.getId(), exhibit.contents().getName(),
        exhibit.contents().getDate(), exhibit.isPublished());
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
        .build();
  }

  private void setExhibitPin(boolean categoryType, Exhibit exhibit) {
    PinType newPinType;
    if (categoryType) {
      Optional<Exhibit> pinnedExhibit = exhibitRepository.findPinnedExhibitWithCategory(
          exhibit.getCategory(),
          new PinType[]{PinType.BOTH, PinType.CATEGORY});
      pinnedExhibit.ifPresent(value -> value.updatePinType(value
          .getPinType() == PinType.BOTH ? PinType.HOME : PinType.NONE));

      newPinType = exhibit.getPinType() == PinType.HOME ? PinType.BOTH : PinType.CATEGORY;
    } else {
      Optional<Exhibit> pinnedExhibit = exhibitRepository.findPinnedExhibit(
          new PinType[]{PinType.BOTH, PinType.HOME});
      pinnedExhibit.ifPresent(value -> value.updatePinType(value
          .getPinType() == PinType.BOTH ? PinType.CATEGORY : PinType.NONE));

      newPinType = exhibit.getPinType() == PinType.CATEGORY ? PinType.BOTH : PinType.HOME;
    }
    exhibit.updatePinType(newPinType);
  }

  private void setExhibitNotPin(boolean categoryType, Exhibit exhibit) {
    PinType newPinType;
    if (categoryType) {
      newPinType = exhibit.getPinType() == PinType.BOTH ? PinType.HOME : PinType.NONE;
    } else {
      newPinType = exhibit.getPinType() == PinType.BOTH ? PinType.CATEGORY : PinType.NONE;
    }
    exhibit.updatePinType(newPinType);
  }
}
