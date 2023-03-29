package com.yapp.artie.domain.archive.service;

import com.yapp.artie.domain.archive.domain.category.Category;
import com.yapp.artie.domain.archive.dto.cateogry.CategoryDto;
import com.yapp.artie.domain.archive.dto.cateogry.CreateCategoryRequestDto;
import com.yapp.artie.domain.archive.dto.cateogry.UpdateCategoryRequestDto;
import com.yapp.artie.domain.archive.exception.CategoryAlreadyExistException;
import com.yapp.artie.domain.archive.exception.CategoryNotFoundException;
import com.yapp.artie.domain.archive.exception.ChangeCategoryWrongLengthException;
import com.yapp.artie.domain.archive.exception.ExceededCategoryCountException;
import com.yapp.artie.domain.archive.exception.NotOwnerOfCategoryException;
import com.yapp.artie.domain.archive.repository.CategoryRepository;
import com.yapp.artie.domain.archive.repository.ExhibitRepository;
import com.yapp.artie.domain.user.domain.UserJpaEntity;
import com.yapp.artie.domain.user.service.UserUseCase;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final ExhibitRepository exhibitRepository;
  private final UserUseCase userService;
  private final int CATEGORY_LIMIT_COUNT = 5;

  public Category findCategoryWithUser(Long id, Long userId) {
    Category category = Optional.ofNullable(categoryRepository.findCategoryEntityGraphById(id))
        .orElseThrow(CategoryNotFoundException::new);
    UserJpaEntity user = findUser(userId);
    validateOwnedByUser(category, user);

    return category;
  }

  public List<CategoryDto> categoriesOf(Long userId) {
    UserJpaEntity user = findUser(userId);
    List<CategoryDto> categories = categoryRepository.findCategoriesByUserOrderBySequence(user)
        .stream().map(this::buildCategoryDto).collect(
            Collectors.toList());
    validateExistAtLeastOneCategory(categories);

    return categories;
  }

  @Transactional
  public Long create(CreateCategoryRequestDto createCategoryRequestDto, Long userId) {
    UserJpaEntity user = findUser(userId);
    String name = createCategoryRequestDto.getName();
    validateDuplicateCategory(name, user);

    return createCategory(name, user).getId();
  }

  @Transactional
  public void delete(Long id, Long userId) {
    UserJpaEntity user = findUser(userId);
    Category category = categoryRepository.findCategoryEntityGraphById(id);
    validateValidPair(user, category);

    categoryRepository.bulkSequenceMinus(user, category.getSequence());
    categoryRepository.deleteById(id);
  }

  @Transactional
  public void update(UpdateCategoryRequestDto updateCategoryRequestDto, Long id, Long userId) {
    UserJpaEntity user = findUser(userId);
    Category category = categoryRepository.findCategoryEntityGraphById(id);
    validateValidPair(user, category);

    category.rename(updateCategoryRequestDto.getName());
  }

  @Transactional
  public void shuffle(List<CategoryDto> changeCategorySequenceDtos, Long userId) {
    UserJpaEntity user = findUser(userId);
    List<Category> categories = categoryRepository.findCategoriesByUserOrderBySequence(user);
    validateChangeCategoriesLengthWithOriginal(changeCategorySequenceDtos, categories);

    int sequence = 0;
    for (CategoryDto changeCategorySequenceDto : changeCategorySequenceDtos) {
      int originSequence = changeCategorySequenceDto.getSequence();
      Category category = categories.get(originSequence);

      category.rearrange(sequence++);
    }
  }

  private Category createCategory(String name, UserJpaEntity user) {
    int sequence = getSequence(user);
    validateExceedLimitCategoryCount(sequence);

    Category category = Category.create(user, name, sequence);
    categoryRepository.save(category);
    return category;
  }

  private UserJpaEntity findUser(Long userId) {
    return userService.findById(userId);
  }

  private int getSequence(UserJpaEntity user) {
    return categoryRepository.countCategoriesByUser(user);
  }

  private void validateExistAtLeastOneCategory(List<CategoryDto> categories) {
    if (categories.size() == 0) {
      throw new CategoryNotFoundException();
    }
  }

  private void validateDuplicateCategory(String name, UserJpaEntity user) {
    List<CategoryDto> categories = categoryRepository.findCategoryDto(user);
    long count = categories.stream()
        .filter(categoryDto -> categoryDto.getName().equals(name))
        .count();

    if (count != 0) {
      throw new CategoryAlreadyExistException();
    }
  }

  private void validateValidPair(UserJpaEntity user, Category category) {
    validateCategoryFound(category);
    validateOwnedByUser(category, user);
  }

  private void validateCategoryFound(Category category) {
    if (category == null) {
      throw new CategoryNotFoundException();
    }
  }

  private void validateOwnedByUser(Category category, UserJpaEntity user) {
    if (!category.ownedBy(user)) {
      throw new NotOwnerOfCategoryException();
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

  private CategoryDto buildCategoryDto(Category category) {
    return new CategoryDto(category.getId(), category.getName(), category.getSequence(),
        exhibitRepository.countExhibitByCategory(category));
  }
}
