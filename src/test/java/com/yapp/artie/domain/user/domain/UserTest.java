package com.yapp.artie.domain.user.domain;

import static com.yapp.artie.common.UserTestData.defaultUser;
import static org.assertj.core.api.Assertions.assertThat;

import com.yapp.artie.domain.user.adapter.out.persistence.UserJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("User 테스트")
public class UserTest {

  public static final UserJpaEntity TEST_USER = UserJpaEntity.create("test-uid", "test-name",
      "test-profile");
  public static final UserJpaEntity TEST_SAVED_USER = UserJpaEntity.create(1L, "test-uid",
      "test-name",
      "test-profile");

  @Test
  void rename_사용자_이름을_변경한다() {
    User user = defaultUser().build();
    user.rename("tomcat");
    assertThat(user.getName()).isEqualTo("tomcat");
  }
}