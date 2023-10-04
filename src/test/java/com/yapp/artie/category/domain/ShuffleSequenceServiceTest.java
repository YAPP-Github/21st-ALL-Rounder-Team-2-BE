package com.yapp.artie.category.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.yapp.artie.category.dto.CategoryDetailResponse;
import com.yapp.artie.category.exception.CategoryNotFoundException;
import com.yapp.artie.category.exception.ChangeCategoryWrongLengthException;
import com.yapp.artie.category.exception.DuplicatedSequenceException;
import com.yapp.artie.user.adapter.out.persistence.UserJpaEntity;
import java.util.List;
import org.junit.jupiter.api.Test;

class ShuffleSequenceServiceTest {

  @Test
  void shuffle_카테고리와_변경될_카테고리_전송_객체_목록을_받아_순서를_변경한다() {
    ShuffleSequenceService shuffleSequenceService = new ShuffleSequenceService();
    UserJpaEntity user = UserJpaEntity.create("", "", "");

    List<Category> categories = List.of(
        Category.create(1L, user, "sample1", 0),
        Category.create(2L, user, "sample2", 1),
        Category.create(3L, user, "sample3", 2)
    );

    List<CategoryDetailResponse> changedCategories = List.of(
        new CategoryDetailResponse(1L, "sample1", 1),
        new CategoryDetailResponse(2L, "sample2", 0),
        new CategoryDetailResponse(3L, "sample3", 2)
    );

    shuffleSequenceService.shuffle(categories, changedCategories);

    int[] expected = new int[]{1, 0, 2};
    int i = 0;
    for (Category category : categories) {
      assertThat(category.getSequence()).isEqualTo(expected[i++]);
    }
  }

  @Test
  void shuffle_변경될_카테고리_리스트의_각_카테고리_아이디는_널값이_될_수_없다() {
    ShuffleSequenceService shuffleSequenceService = new ShuffleSequenceService();
    UserJpaEntity user = UserJpaEntity.create("", "", "");

    List<Category> categories = List.of(
        Category.create(user, "sample1", 0),
        Category.create(user, "sample2", 1),
        Category.create(user, "sample3", 2)
    );

    List<CategoryDetailResponse> changedCategories = List.of(
        new CategoryDetailResponse(1L, "sample1", 1),
        new CategoryDetailResponse(2L, "sample2", 0),
        new CategoryDetailResponse(3L, "sample3", 2)
    );

    assertThatThrownBy(() -> shuffleSequenceService.shuffle(categories, changedCategories))
        .isInstanceOf(CategoryNotFoundException.class)
        .hasMessage("카테고리가 존재하지 않습니다.");
  }

  @Test
  void shuffle_변경될_카테고리의_순서들은_겹치는_것이_없어야_한다() {
    ShuffleSequenceService shuffleSequenceService = new ShuffleSequenceService();
    UserJpaEntity user = UserJpaEntity.create("", "", "");

    List<Category> categories = List.of(
        Category.create(1L, user, "sample1", 0),
        Category.create(2L, user, "sample2", 1)
    );

    List<CategoryDetailResponse> changedCategories = List.of(
        new CategoryDetailResponse(1L, "sample1", 0),
        new CategoryDetailResponse(2L, "sample2", 0)
    );

    assertThatThrownBy(() -> shuffleSequenceService.shuffle(categories, changedCategories))
        .isInstanceOf(DuplicatedSequenceException.class)
        .hasMessage("중복된 순서가 존재합니다.");
  }

  @Test
  void shuffle_원본_카테고리와_변경_요청된_카테고리_전송_객체_목록의_수가_같아야한다() {
    ShuffleSequenceService shuffleSequenceService = new ShuffleSequenceService();
    UserJpaEntity user = UserJpaEntity.create("", "", "");

    List<Category> categories = List.of(
        Category.create(user, "sample1", 0),
        Category.create(user, "sample2", 1),
        Category.create(user, "sample3", 2)
    );

    List<CategoryDetailResponse> changedCategories1 = List.of(
        new CategoryDetailResponse(1L, "sample1", 1),
        new CategoryDetailResponse(2L, "sample2", 0)
    );

    List<CategoryDetailResponse> changedCategories2 = List.of(
        new CategoryDetailResponse(1L, "sample1", 1),
        new CategoryDetailResponse(2L, "sample2", 0),
        new CategoryDetailResponse(3L, "sample3", 2),
        new CategoryDetailResponse(4L, "sample4", 3)
    );

    assertThatThrownBy(() -> shuffleSequenceService.shuffle(categories, changedCategories1))
        .isInstanceOf(ChangeCategoryWrongLengthException.class)
        .hasMessage("수정할 카테고리의 개수는 원본의 개수와 같아야 합니다.");

    assertThatThrownBy(() -> shuffleSequenceService.shuffle(categories, changedCategories2))
        .isInstanceOf(ChangeCategoryWrongLengthException.class)
        .hasMessage("수정할 카테고리의 개수는 원본의 개수와 같아야 합니다.");
  }
}
