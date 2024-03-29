package com.yapp.artie.category.service;

import com.yapp.artie.category.domain.Category;
import com.yapp.artie.category.domain.CategoryRepository;
import com.yapp.artie.category.domain.ShuffleSequenceService;
import com.yapp.artie.category.dto.CategoryDetailResponse;
import com.yapp.artie.category.dto.CreateCategoryRequest;
import com.yapp.artie.category.dto.UpdateCategoryRequest;
import com.yapp.artie.category.exception.CategoryAlreadyExistException;
import com.yapp.artie.category.exception.CategoryNotFoundException;
import com.yapp.artie.category.exception.ExceededCategoryCountException;
import com.yapp.artie.gallery.domain.repository.ExhibitionRepository;
import com.yapp.artie.global.deprecated.LoadUserJpaEntityApi;
import com.yapp.artie.user.adapter.out.persistence.UserJpaEntity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

  private static final int CATEGORY_LIMIT_COUNT = 5;

  private final CategoryRepository categoryRepository;
  private final ExhibitionRepository exhibitionRepository;
  private final LoadUserJpaEntityApi loadUserJpaEntityApi;
  private final ShuffleSequenceService shuffleSequenceService;

  public Category findCategoryWithUser(Long id, Long userId) {
    return findUserCategory(id, userId);
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
    Category category = findUserCategory(id, userId);

    categoryRepository.bulkSequenceMinus(user, category.getSequence());
    categoryRepository.deleteById(id);
  }

  @Transactional
  public void update(UpdateCategoryRequest updateCategoryRequest, Long id, Long userId) {
    Category category = findUserCategory(id, userId);

    category.rename(updateCategoryRequest.getName());
  }

  @Transactional
  public void shuffle(List<CategoryDetailResponse> changedCategories, Long userId) {
    UserJpaEntity user = findUser(userId);
    List<Category> categories = categoryRepository.findCategoriesByUser(user);

    shuffleSequenceService.shuffle(categories, changedCategories);
  }

  private Category createCategory(String name, UserJpaEntity user) {
    int sequence = getSequence(user);
    validateExceedLimitCategoryCount(sequence);

    Category category = Category.create(user, name, sequence);
    categoryRepository.save(category);
    return category;
  }

  private Category findUserCategory(Long id, Long userId) {
    return categoryRepository.findUserCategory(id, userId)
        .orElseThrow(CategoryNotFoundException::new);
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

  private void validateExceedLimitCategoryCount(int sequence) {
    if (sequence >= CATEGORY_LIMIT_COUNT) {
      throw new ExceededCategoryCountException();
    }
  }

  private CategoryDetailResponse buildCategoryDto(Category category) {
    return new CategoryDetailResponse(category.getId(), category.getName(), category.getSequence(),
        exhibitionRepository.countExhibitionByCategory(category));
  }
}
