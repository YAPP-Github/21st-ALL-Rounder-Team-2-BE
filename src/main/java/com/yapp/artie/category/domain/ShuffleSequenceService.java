package com.yapp.artie.category.domain;

import com.yapp.artie.category.dto.CategoryDetailResponse;
import com.yapp.artie.category.exception.CategoryNotFoundException;
import com.yapp.artie.category.exception.ChangeCategoryWrongLengthException;
import com.yapp.artie.category.exception.DuplicatedSequenceException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ShuffleSequenceService {

  public void shuffle(List<Category> categories, List<CategoryDetailResponse> changedCategories) {
    validateDuplicatedSequence(changedCategories);
    validateChangedCategoriesLength(categories, changedCategories);

    changedCategories.forEach(changedCategory -> {
      Category category = findOriginalCategory(categories, changedCategory);

      category.rearrange(changedCategory.getSequence());
    });
  }

  private void validateDuplicatedSequence(List<CategoryDetailResponse> changedCategories) {
    Set<Integer> set = changedCategories.stream()
        .mapToInt(CategoryDetailResponse::getSequence)
        .boxed()
        .collect(Collectors.toSet());

    if (set.size() != changedCategories.size()) {
      throw new DuplicatedSequenceException();
    }
  }

  private void validateChangedCategoriesLength(List<Category> categories,
      List<CategoryDetailResponse> changedCategories) {
    if (changedCategories.size() != categories.size()) {
      throw new ChangeCategoryWrongLengthException();
    }
  }

  private Category findOriginalCategory(List<Category> categories,
      CategoryDetailResponse changedCategory) {
    return categories.stream()
        .filter(originCategory -> isChangeable(changedCategory, originCategory))
        .findFirst()
        .orElseThrow(CategoryNotFoundException::new);
  }

  private boolean isChangeable(CategoryDetailResponse changedCategory,
      Category originCategory) {
    return Objects.equals(originCategory.getId(), changedCategory.getId());
  }
}
