package com.yapp.artie.domain.user.application.service;

import static com.yapp.artie.common.UserTestData.defaultUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.yapp.artie.domain.user.application.port.in.RegisterUserResponse;
import com.yapp.artie.domain.user.application.port.out.LoadUserPort;
import com.yapp.artie.domain.user.application.port.out.SaveUserPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RegisterUserServiceTest {

  private final LoadUserPort loadUserPort = Mockito.mock(LoadUserPort.class);
  private final SaveUserPort saveUserPort = Mockito.mock(SaveUserPort.class);
  private final RegisterUserService registerUserService = new RegisterUserService(loadUserPort,
      saveUserPort);

  @Test
  void register_이미_등록된_사용자라면_ID를_그대로_반환한다() {
    givenUser();
    RegisterUserResponse actual = registerUserService.register("1", "test", null);
    assertThat(actual.getId()).isEqualTo(1L);
  }

  @Test
  void register_이미_등록된_사용자라면_저장소에_저장하지_않는다() {
    givenUser();
    registerUserService.register("1", "test", null);
    then(saveUserPort)
        .should(never())
        .save(any());
  }

  @Test
  void register_신규_사용자라면_새로운_id를_부여받는다() {
    Long expectedId = 2L;
    givenUserFindWithUidWillFail();
    givenSaveWillReturnId(expectedId);
    RegisterUserResponse actual = registerUserService.register("1", "test", null);
    assertThat(actual.getId()).isEqualTo(expectedId);
    then(saveUserPort)
        .should()
        .save(any());
  }

  private void givenUser() {
    given(loadUserPort.loadByUid(any()))
        .willReturn(defaultUser().build());
  }

  private void givenSaveWillReturnId(Long id) {
    given(saveUserPort.save(any()))
        .willReturn(id);
  }

  private void givenUserFindWithUidWillFail() {
    given(loadUserPort.loadByUid(any()))
        .willThrow(UserNotFoundException.class);
  }
}