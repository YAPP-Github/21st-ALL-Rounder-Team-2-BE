package com.yapp.artie.domain.archive.service;

import com.yapp.artie.domain.archive.domain.category.Category;
import com.yapp.artie.domain.archive.dto.cateogry.CategoryDto;
import com.yapp.artie.domain.archive.dto.cateogry.CreateCategoryRequestDto;
import com.yapp.artie.domain.archive.dto.cateogry.UpdateCategoryRequestDto;
import com.yapp.artie.domain.archive.exception.CategoryAlreadyExistException;
import com.yapp.artie.domain.archive.exception.CategoryNotFoundException;
import com.yapp.artie.domain.archive.exception.ChangeCategoryWrongLengthException;
import com.yapp.artie.domain.archive.exception.ChangeDefaultCategoryException;
import com.yapp.artie.domain.archive.exception.ExceededCategoryCountException;
import com.yapp.artie.domain.archive.exception.NotOwnerOfCategoryException;
import com.yapp.artie.domain.archive.repository.CategoryRepository;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.exception.UserNotFoundException;
import com.yapp.artie.domain.user.service.UserService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final UserService userService;
  private final String DEFAULT_CATEGORY_NAME = "전체 기록";
  private final int CATEGORY_LIMIT_COUNT = 5;

  public Category findCategoryWithUser(Long id) {
    return Optional.ofNullable(categoryRepository.findCategoryEntityGraphById(id))
        .orElseThrow(CategoryNotFoundException::new);
  }

  public List<CategoryDto> categoriesOf(Long userId) {
    User user = findUser(userId);
    List<CategoryDto> categories = categoryRepository.findCategoryDto(user);
    validateExistAtLeastOneCategory(categories);

    return categories;
  }

  @Transactional
  public Long createDefault(Long userId) {
    User user = findUser(userId);
    validateDuplicateCategory(DEFAULT_CATEGORY_NAME, user);

    return createCategory(DEFAULT_CATEGORY_NAME, user).getId();
  }

  @Transactional
  public Long create(CreateCategoryRequestDto createCategoryRequestDto, Long userId) {
    User user = findUser(userId);
    String name = createCategoryRequestDto.getName();
    validateDuplicateCategory(name, user);

    return createCategory(name, user).getId();
  }

  @Transactional
  public void delete(Long id, Long userId) {
    User user = findUser(userId);
    Category category = categoryRepository.findCategoryEntityGraphById(id);
    validateValidPair(user, category);
    validateDefaultCategory(category);

    categoryRepository.bulkSequenceMinus(user, category.getSequence());
    categoryRepository.deleteById(id);
  }

  @Transactional
  public void update(UpdateCategoryRequestDto updateCategoryRequestDto, Long id, Long userId) {
    User user = findUser(userId);
    Category category = categoryRepository.findCategoryEntityGraphById(id);
    validateValidPair(user, category);
    validateDefaultCategory(category);

    category.rename(updateCategoryRequestDto.getName());
  }

  @Transactional
  public void shuffle(List<CategoryDto> changeCategorySequenceDtos, Long userId) {
    User user = findUser(userId);
    List<Category> categories = categoryRepository.findCategoriesByUser(user);
    validateChangeCategoriesLengthWithOriginal(changeCategorySequenceDtos, categories);

    int sequence = 1;
    for (CategoryDto changeCategorySequenceDto : changeCategorySequenceDtos) {
      int originSequence = changeCategorySequenceDto.getSequence();
      Category category = categories.get(originSequence);

      category.rearrange(sequence++);
    }
  }

  private Category createCategory(String name, User user) {
    int sequence = categoryRepository.countCategoriesByUser(user);
    validateExceedLimitCategoryCount(sequence);

    Category category = Category.create(user, name, sequence);
    categoryRepository.save(category);
    return category;
  }

  private User findUser(Long userId) {
    return userService.findById(userId)
        .orElseThrow(UserNotFoundException::new);
  }

  private void validateExistAtLeastOneCategory(List<CategoryDto> categories) {
    if (categories.size() == 0) {
      throw new CategoryNotFoundException();
    }
  }

  private void validateDuplicateCategory(String name, User user) {
    List<CategoryDto> categories = categoryRepository.findCategoryDto(user);
    long count = categories.stream()
        .filter(categoryDto -> categoryDto.getName().equals(name))
        .count();

    if (count != 0) {
      throw new CategoryAlreadyExistException();
    }
  }

  private void validateValidPair(User user, Category category) {
    validateCategoryFound(category);
    validateOwnedByUser(category, user);
  }

  private void validateCategoryFound(Category category) {
    if (category == null) {
      throw new CategoryNotFoundException();
    }
  }

  private void validateOwnedByUser(Category category, User user) {
    if (!category.ownedBy(user)) {
      throw new NotOwnerOfCategoryException();
    }
  }

  private void validateDefaultCategory(Category category) {
    if (category.getName().equals(DEFAULT_CATEGORY_NAME)) {
      throw new ChangeDefaultCategoryException();
    }
  }

  private void validateExceedLimitCategoryCount(int sequence) {
    if (sequence >= CATEGORY_LIMIT_COUNT) {
      throw new ExceededCategoryCountException();
    }
  }

  private void validateChangeCategoriesLengthWithOriginal(
      List<CategoryDto> changeCategorySequenceDtos,
      List<Category> categories) {
    if (categories.size() != changeCategorySequenceDtos.size()) {
      throw new ChangeCategoryWrongLengthException();
    }
  }
}
