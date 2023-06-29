package com.yapp.artie.user.application.service;

import static com.yapp.artie.common.UserTestData.defaultUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.yapp.artie.common.BaseUserUnitTest;
import com.yapp.artie.user.domain.User;
import com.yapp.artie.user.domain.UserNotFoundException;
import org.junit.jupiter.api.Test;

class GetUserServiceTest extends BaseUserUnitTest {

  private final GetUserService getUserService = new GetUserService(loadUserPort);

  @Test
  void loadUserById_사용자를_찾을_수_없으면_예외를_발생한다() {
    givenUserFindWillFail();
    assertThatThrownBy(() -> getUserService.loadUserById(1L))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void loadUserById_사용자를_조회한다() {
    User user = defaultUser().build();
    givenUserByReference(user);
    User actual = getUserService.loadUserById(1L);
    assertThat(actual.getId()).isEqualTo(user.getId());
  }
}