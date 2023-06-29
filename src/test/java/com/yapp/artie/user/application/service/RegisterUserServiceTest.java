package com.yapp.artie.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.yapp.artie.common.BaseUserUnitTest;
import com.yapp.artie.user.application.port.in.response.RegisterUserResponse;
import org.junit.jupiter.api.Test;

class RegisterUserServiceTest extends BaseUserUnitTest {

  private final RegisterUserService registerUserService = new RegisterUserService(loadUserPort,
      saveUserPort);

  @Test
  void register_이미_등록된_사용자라면_ID를_그대로_반환한다() {
    givenUserFindWithUid();
    RegisterUserResponse actual = registerUserService.register("1", "test", null);
    assertThat(actual.getId()).isEqualTo(1L);
  }

  @Test
  void register_이미_등록된_사용자라면_저장소에_저장하지_않는다() {
    givenUserFindWithUid();
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
}