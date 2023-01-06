package com.yapp.artie.domain.archive.service;

import com.yapp.artie.domain.archive.dto.CategoryDto;
import com.yapp.artie.domain.archive.exception.NotExsistCategoryException;
import com.yapp.artie.domain.archive.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public List<CategoryDto> categories(Long uid) {
    List<CategoryDto> categories = categoryRepository.findCategoryDto(uid);
    validateCategoryLength(categories);

    return categories;
  }

  private void validateCategoryLength(List<CategoryDto> categories) {
    if (categories.size() == 0) {
      throw new NotExsistCategoryException();
    }
  }
}
