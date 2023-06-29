package com.yapp.artie.category.service;

import com.yapp.artie.category.domain.Category;
import com.yapp.artie.category.dto.CategoryDetailResponse;
import com.yapp.artie.category.dto.CreateCategoryRequest;
import com.yapp.artie.category.dto.UpdateCategoryRequest;
import com.yapp.artie.category.exception.CategoryAlreadyExistException;
import com.yapp.artie.category.exception.CategoryNotFoundException;
import com.yapp.artie.category.exception.ChangeCategoryWrongLengthException;
import com.yapp.artie.category.exception.ExceededCategoryCountException;
import com.yapp.artie.category.exception.NotOwnerOfCategoryException;
import com.yapp.artie.category.repository.CategoryRepository;
import com.yapp.artie.gallery.domain.repository.ExhibitionRepository;
import com.yapp.artie.global.deprecated.LoadUserJpaEntityApi;
import com.yapp.artie.user.adapter.out.persistence.UserJpaEntity;
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
  private final ExhibitionRepository exhibitionRepository;
  private final LoadUserJpaEntityApi loadUserJpaEntityApi;
  private final int CATEGORY_LIMIT_COUNT = 5;

  public Category findCategoryWithUser(Long id, Long userId) {
    Category category = Optional.ofNullable(categoryRepository.findCategoryEntityGraphById(id))
        .orElseThrow(CategoryNotFoundException::new);
    UserJpaEntity user = findUser(userId);
    validateOwnedByUser(category, user);

    return category;
  }

  public List<CategoryDetailResponse> categoriesOf(Long userId) {
    return categoryRepository.findCategoriesByUserOrderBySequence(findUser(userId))
        .stream()
        .map(this::buildCategoryDto)
        .collect(Collectors.toList());
  }

  @Transactional
  public Long create(CreateCategoryRequest createCategoryRequest, Long userId) {
    UserJpaEntity user = findUser(userId);
    String name = createCategoryRequest.getName();
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
  public void update(UpdateCategoryRequest updateCategoryRequest, Long id, Long userId) {
    UserJpaEntity user = findUser(userId);
    Category category = categoryRepository.findCategoryEntityGraphById(id);
    validateValidPair(user, category);

    category.rename(updateCategoryRequest.getName());
  }

  @Transactional
  public void shuffle(List<CategoryDetailResponse> changeCategorySequenceDtos, Long userId) {
    UserJpaEntity user = findUser(userId);
    List<Category> categories = categoryRepository.findCategoriesByUser(user);
    validateChangeCategoriesLengthWithOriginal(changeCategorySequenceDtos, categories);

    for (CategoryDetailResponse changeCategorySequenceDto : changeCategorySequenceDtos) {
      categoryRepository.findById(changeCategorySequenceDto.getId())
          .ifPresent(value -> {
            if (value.ownedBy(user)) {
              value.rearrange(changeCategorySequenceDto.getSequence());
            }
          });
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
    return loadUserJpaEntityApi.findById(userId);
  }

  private int getSequence(UserJpaEntity user) {
    return categoryRepository.countCategoriesByUser(user);
  }

  private void validateDuplicateCategory(String name, UserJpaEntity user) {
    List<CategoryDetailResponse> categories = categoryRepository.findCategoryDto(user);
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
      List<CategoryDetailResponse> changeCategorySequenceDtos,
      List<Category> categories) {
    if (categories.size() != changeCategorySequenceDtos.size()) {
      throw new ChangeCategoryWrongLengthException();
    }
  }

  private CategoryDetailResponse buildCategoryDto(Category category) {
    return new CategoryDetailResponse(category.getId(), category.getName(), category.getSequence(),
        exhibitionRepository.countExhibitionByCategory(category));
  }
}
