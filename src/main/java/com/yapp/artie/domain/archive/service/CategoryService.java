package com.yapp.artie.domain.archive.service;

import com.yapp.artie.domain.archive.domain.category.Category;
import com.yapp.artie.domain.archive.dto.cateogry.CategoryDto;
import com.yapp.artie.domain.archive.dto.cateogry.CreateCategoryRequestDto;
import com.yapp.artie.domain.archive.dto.cateogry.UpdateCategoryRequestDto;
import com.yapp.artie.domain.archive.exception.CategoryAlreadyExistException;
import com.yapp.artie.domain.archive.exception.CategoryNotFoundException;
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
  public Long create(CreateCategoryRequestDto createCategoryRequestDto, Long userId) {
    User user = findUser(userId);
    String name = createCategoryRequestDto.getName();
    validateDuplicateCategory(name, user);

    Category category = Category.create(user, name);
    categoryRepository.save(category);

    return category.getId();
  }

  @Transactional
  public void delete(Long id, Long userId) {
    User user = findUser(userId);
    Category category = categoryRepository.findCategoryEntityGraphById(id);
    validate(user, category);

    categoryRepository.deleteById(id);
  }

  @Transactional
  public void update(UpdateCategoryRequestDto updateCategoryRequestDto, Long id, Long userId) {
    User user = findUser(userId);
    Category category = categoryRepository.findCategoryEntityGraphById(id);
    validate(user, category);

    category.update(updateCategoryRequestDto.getName());
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

  private void validate(User user, Category category) {
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
}
