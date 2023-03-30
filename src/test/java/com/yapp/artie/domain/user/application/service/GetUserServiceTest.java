package com.yapp.artie.domain.user.application.service;

import static com.yapp.artie.common.UserTestData.defaultUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.yapp.artie.domain.user.application.port.out.LoadUserPort;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;

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

  @Test
  void loadUserByUsername_사용자를_조회했을_때_반환되는_객체의_ID는_엔티티의_식별자와_같다() {
    User user = defaultUser().build();
    givenUserByReferenceWithUid(user);
    UserDetails actual = getUserService.loadUserByUsername(user.getUid());
    assertThat(actual.getUsername()).isEqualTo(user.getId().toString());
  }

  @Test
  void loadUserByUsername_사용자를_조회했을_때_반환되는_객체의_PW는_엔티티의_UID와_같다() {
    User user = defaultUser().build();
    givenUserByReferenceWithUid(user);
    UserDetails actual = getUserService.loadUserByUsername(user.getUid());
    assertThat(actual.getPassword()).isEqualTo(user.getUid());
  }

  private void givenUserByReference(User user) {
    given(loadUserPort.loadById(any()))
        .willReturn(user);
  }

  private void givenUserByReferenceWithUid(User user) {
    given(loadUserPort.loadByUid(any()))
        .willReturn(user);
  }

  private void givenUserFindWillFail() {
    given(loadUserPort.loadById(any()))
        .willThrow(UserNotFoundException.class);
  }
}