package com.yapp.artie.domain.archive.service;

import com.yapp.artie.domain.archive.domain.category.Category;
import com.yapp.artie.domain.archive.domain.exhibit.Exhibit;
import com.yapp.artie.domain.archive.domain.exhibit.ExhibitContents;
import com.yapp.artie.domain.archive.dto.exhibit.CreateExhibitRequestDto;
import com.yapp.artie.domain.archive.dto.exhibit.PostInfoDto;
import com.yapp.artie.domain.archive.dto.exhibit.UpdateExhibitRequestDto;
import com.yapp.artie.domain.archive.exception.ExhibitNotFoundException;
import com.yapp.artie.domain.archive.exception.NotOwnerOfCategoryException;
import com.yapp.artie.domain.archive.exception.NotOwnerOfExhibitException;
import com.yapp.artie.domain.archive.repository.ExhibitRepository;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.exception.UserNotFoundException;
import com.yapp.artie.domain.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExhibitService {

  private final ExhibitRepository exhibitRepository;
  private final UserService userService;
  private final CategoryService categoryService;

  public PostInfoDto getExhibitInformation(Long id, Long userId) {
    User user = findUser(userId);
    Exhibit exhibit = exhibitRepository.findExhibitEntityGraphById(id)
        .orElseThrow(ExhibitNotFoundException::new);
    validateOwnedByUser(user, exhibit);

    ExhibitContents contents = exhibit.contents();
    return new PostInfoDto(exhibit.getId(), contents.getName(),
        contents.getDate(), exhibit.isPublished());
  }

  public List<PostInfoDto> getDraftExhibits(Long userId) {
    return exhibitRepository.findExhibitDto(findUser(userId));
  }

  public Page<PostInfoDto> getExhibitByPage(Long id, Long userId, Pageable pageable) {
    User user = findUser(userId);
    Category category = categoryService.findCategoryWithUser(userId);
    validateCategoryOwnedByUser(category, user);

    return exhibitRepository.findExhibitAllCountBy(pageable, user, category)
        .map(exhibit -> {
          ExhibitContents contents = exhibit.contents();
          return new PostInfoDto(exhibit.getId(), contents.getName(), contents.getDate(),
              exhibit.isPublished());
        });
  }

  @Transactional
  public Long create(CreateExhibitRequestDto createExhibitRequestDto, Long userId) {
    User user = findUser(userId);
    Category category = categoryService.findCategoryWithUser(
        createExhibitRequestDto.getCategoryId());

    if (!category.ownedBy(user)) {
      throw new NotOwnerOfCategoryException();
    }

    Exhibit exhibit = Exhibit.create(createExhibitRequestDto.getName(),
        createExhibitRequestDto.getPostDate(), category, user);
    exhibitRepository.save(exhibit);

    return exhibit.getId();
  }

  @Transactional
  public void publish(Long id, Long userId) {
    User user = findUser(userId);
    Exhibit exhibit = exhibitRepository.findExhibitEntityGraphById(id)
        .orElseThrow(ExhibitNotFoundException::new);
    validateOwnedByUser(user, exhibit);

    exhibit.publish();
  }

  @Transactional
  public void update(UpdateExhibitRequestDto updateExhibitRequestDto, Long id, Long userId) {
    User user = findUser(userId);
    Exhibit exhibit = exhibitRepository.findExhibitEntityGraphById(id)
        .orElseThrow(ExhibitNotFoundException::new);
    validateOwnedByUser(user, exhibit);

    exhibit.update(updateExhibitRequestDto.getName(), updateExhibitRequestDto.getPostDate());
  }

  private User findUser(Long userId) {
    return userService.findById(userId)
        .orElseThrow(UserNotFoundException::new);
  }

  private void validateOwnedByUser(User user, Exhibit exhibit) {
    if (!exhibit.ownedBy(user)) {
      throw new NotOwnerOfExhibitException();
    }
  }

  //TODO : 리팩토링 신호
  private void validateCategoryOwnedByUser(Category category, User user) {
    if (!category.ownedBy(user)) {
      throw new NotOwnerOfCategoryException();
    }
  }
}
