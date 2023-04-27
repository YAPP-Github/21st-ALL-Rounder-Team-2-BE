package com.yapp.artie.domain.user.domain;

import static com.yapp.artie.common.UserTestData.defaultUser;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UserTest {

  @Test
  void rename_사용자_이름을_변경한다() {
    User user = defaultUser().build();
    user.rename("tomcat");
    assertThat(user.getName()).isEqualTo("tomcat");
  }
}