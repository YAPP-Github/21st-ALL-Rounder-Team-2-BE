package com.yapp.artie.category.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.yapp.artie.category.domain.exception.InvalidCategoryNameException;
import com.yapp.artie.category.domain.exception.InvalidSequenceException;
import com.yapp.artie.user.adapter.out.persistence.UserJpaEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class CategoryTest {

  @ParameterizedTest
  @ValueSource(ints = {-1, 5})
  void sequence_카테고리의_순서는_0부터_4사이의_숫자이다(int source) {
    assertThatThrownBy(() -> Category.create(UserJpaEntity.create("", "", ""), "sample", source))
        .isInstanceOf(InvalidSequenceException.class)
        .hasMessage("카테고리의 순서는 0-4 사이의 숫자입니다.");
  }

  @Test
  void rearrange_카테고리의_순서를_변경할_수_있다() {
    Category category = Category.create(UserJpaEntity.create("", "", ""), "sample", 1);

    category.rearrange(3);

    assertThat(category.getSequence()).isEqualTo(3);
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 5})
  void rearrange_카테고리의_순서_변경_시_0미만_5이상의_숫자는_허용되지_않는다(int source) {
    Category category = Category.create(UserJpaEntity.create("", "", ""), "sample", 1);

    assertThatThrownBy(() -> category.rearrange(source))
        .isInstanceOf(InvalidSequenceException.class)
        .hasMessage("카테고리의 순서는 0-4 사이의 숫자입니다.");
  }

  @ParameterizedTest
  @NullAndEmptySource
  void name_카테고리의_이름은_적어도_한_글자_이상이어야_한다(String source) {
    assertThatThrownBy(() -> Category.create(UserJpaEntity.create("", "", ""), source, 1))
        .isInstanceOf(InvalidCategoryNameException.class)
        .hasMessage("카테고리의 이름 길이는 1-20자입니다.");
  }

  @Test
  void name_카테고리의_이름은_최대_20_글자_이하이다() {
    assertThatThrownBy(() -> Category.create(UserJpaEntity.create("", "", ""), "a".repeat(21), 1))
        .isInstanceOf(InvalidCategoryNameException.class)
        .hasMessage("카테고리의 이름 길이는 1-20자입니다.");
  }

  @Test
  void rename_카테고리의_이름을_변경할_수_있다() {
    Category category = Category.create(UserJpaEntity.create("", "", ""), "sample", 1);

    category.rename("renamed");

    assertThat(category.getName()).isEqualTo("renamed");
  }

  @ParameterizedTest
  @NullAndEmptySource
  void rename_변경할_카테고리의_이름은_적어도_한_글자_이상이어야_한다(String source) {
    Category category = Category.create(UserJpaEntity.create("", "", ""), "sample", 1);

    assertThatThrownBy(() -> category.rename(source))
        .isInstanceOf(InvalidCategoryNameException.class)
        .hasMessage("카테고리의 이름 길이는 1-20자입니다.");
  }

  @Test
  void rename_변경할_카테고리의_이름은_최대_20글자를_초과할_수_없다() {
    Category category = Category.create(UserJpaEntity.create("", "", ""), "sample", 1);

    assertThatThrownBy(() -> category.rename("1".repeat(21)))
        .isInstanceOf(InvalidCategoryNameException.class)
        .hasMessage("카테고리의 이름 길이는 1-20자입니다.");
  }
}
