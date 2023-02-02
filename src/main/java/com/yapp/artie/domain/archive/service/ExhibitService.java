package com.yapp.artie.domain.archive.service;

import com.yapp.artie.domain.archive.domain.artwork.Artwork;
import com.yapp.artie.domain.archive.domain.artwork.NullArtwork;
import com.yapp.artie.domain.archive.domain.category.Category;
import com.yapp.artie.domain.archive.domain.exhibit.Exhibit;
import com.yapp.artie.domain.archive.dto.exhibit.CalendarExhibitRequestDto;
import com.yapp.artie.domain.archive.dto.exhibit.CalendarExhibitResponseDto;
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
public class ExhibitService {

  @Value("${cloud.aws.cloudfront.domain}")
  private String cdnDomain;

  private final ExhibitRepository exhibitRepository;
  private final ArtworkRepository artworkRepository;
  private final UserService userService;
  private final CategoryService categoryService;

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

  public Page<PostDetailInfo> getExhibitByPage(Long id, Long userId, Pageable pageable) {
    Category category = categoryService.findCategoryWithUser(id, userId);
    return exhibitRepository.findExhibitAllCountBy(pageable, findUser(userId), category)
        .map(exhibit -> buildDetailExhibitionInformation(exhibit, getMainImageUri(exhibit)));
  }

  public List<CalendarExhibitResponseDto> getExhibitByMonthly(
      CalendarExhibitRequestDto calendarExhibitRequestDto, Long userId) {
    User user = findUser(userId);
    int year = calendarExhibitRequestDto.getYear();
    int month = calendarExhibitRequestDto.getMonth();

    return exhibitRepository.findAllExhibitForCalendar(
            DateUtils.getFirstDayOf(year, month), DateUtils.getLastDayOf(year, month), user).stream()
        .map(this::buildCalendarExhibitInformation)
        .collect(Collectors.toList());
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

    exhibit.update(updateExhibitRequestDto.getName(), updateExhibitRequestDto.getPostDate());
  }

  public Exhibit getExhibitByUser(Long id, Long userId) {
    Exhibit exhibit = exhibitRepository.findExhibitEntityGraphById(id)
        .orElseThrow(ExhibitNotFoundException::new);
    validateOwnedByUser(findUser(userId), exhibit);
    return exhibit;
  }

  private User findUser(Long userId) {
    return userService.findById(userId);
  }

  private String getMainImageUri(Exhibit exhibit) {
    return artworkRepository.findMainArtworkByExhibitId(exhibit)
        .map(artwork -> artwork.getContents().getFullUri(cdnDomain)).orElse(null);
  }

  // TODO : public이 아니도록 수정
  public void validateOwnedByUser(User user, Exhibit exhibit) {
    if (!exhibit.ownedBy(user)) {
      throw new NotOwnerOfExhibitException();
    }
  }

  private CalendarExhibitResponseDto buildCalendarExhibitInformation(Exhibit exhibit) {
    Artwork mainArtwork = artworkRepository.findMainArtworkByExhibitId(exhibit)
        .orElseGet(NullArtwork::create);

    return new CalendarExhibitResponseDto(
        exhibit.contents().getDate().getYear(),
        exhibit.contents().getDate().getMonthValue(),
        exhibit.contents().getDate().getDayOfMonth(),
        mainArtwork.getContents().getFullUri(cdnDomain),
        exhibit.isPublished());
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
}
