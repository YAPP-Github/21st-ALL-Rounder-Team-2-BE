package com.yapp.artie.domain.user.application.service;

import static com.yapp.artie.common.UserTestData.defaultUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.yapp.artie.domain.user.application.port.out.LoadUserPort;
import com.yapp.artie.domain.user.application.port.out.UpdateUserStatePort;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.domain.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RenameUserServiceTest {

  private final UpdateUserStatePort updateUserStatePort = Mockito.mock(UpdateUserStatePort.class);
  private final LoadUserPort loadUserPort = Mockito.mock(LoadUserPort.class);
  private final RenameUserService renameUserService = new RenameUserService(loadUserPort,
      updateUserStatePort);

  @Test
  void rename_사용자를_찾을_수_없으면_예외를_발생한다() {
    givenUserFindWillFail();
    assertThatThrownBy(() -> renameUserService.rename(1L, "test"))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void rename_주어진_이름으로_사용자의_이름이_변경된다() {
    User user = defaultUser().build();
    givenUserByReference(user);

    String beforeName = loadUserPort
        .loadById(1L)
        .getName();

    String expectedName = "tomcat";
    renameUserService.rename(1L, expectedName);

    assertThat(user.getName()).isEqualTo(expectedName);
    assertThat(user.getName()).isNotEqualTo(beforeName);
  }

  private void givenUserByReference(User user) {
    given(loadUserPort.loadById(any()))
        .willReturn(user);
  }

  private void givenUserFindWillFail() {
    given(loadUserPort.loadById(any()))
        .willThrow(UserNotFoundException.class);
  }
}