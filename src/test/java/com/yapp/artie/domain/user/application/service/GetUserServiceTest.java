package com.yapp.artie.domain.user.application.service;

import static com.yapp.artie.common.UserTestData.defaultUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.yapp.artie.domain.user.application.port.out.LoadUserPort;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GetUserServiceTest {

  private final LoadUserPort loadUserPort = Mockito.mock(LoadUserPort.class);
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

  private void givenUserByReference(User user) {
    given(loadUserPort.loadById(any()))
        .willReturn(user);
  }

  private void givenUser() {
    given(loadUserPort.loadById(any()))
        .willReturn(defaultUser().build());
  }

  private void givenUserFindWillFail() {
    given(loadUserPort.loadById(any()))
        .willThrow(UserNotFoundException.class);
  }
}