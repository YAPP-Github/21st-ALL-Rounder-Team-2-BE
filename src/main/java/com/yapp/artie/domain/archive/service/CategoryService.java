package com.yapp.artie.domain.archive.service;

import com.yapp.artie.domain.archive.domain.category.Category;
import com.yapp.artie.domain.archive.dto.cateogry.CategoryDto;
import com.yapp.artie.domain.archive.dto.cateogry.CreateCategoryRequestDto;
import com.yapp.artie.domain.archive.exception.NotExsistCategoryException;
import com.yapp.artie.domain.archive.repository.CategoryRepository;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final UserService userService;

  public List<CategoryDto> categories(Long userId) {
    List<CategoryDto> categories = categoryRepository.findCategoryDto(userId);
    validateCategoryLength(categories);

    return categories;
  }

  @Transactional
  public Long create(CreateCategoryRequestDto createCategoryRequestDto, Long userId) {
    User user = userService.findById(userId).get();
    Category category = Category.create(user, createCategoryRequestDto.getName());
    categoryRepository.save(category);

    return category.getId();
  }

  private void validateCategoryLength(List<CategoryDto> categories) {
    if (categories.size() == 0) {
      throw new NotExsistCategoryException();
    }
  }

}
