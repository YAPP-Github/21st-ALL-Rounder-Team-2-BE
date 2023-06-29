package com.yapp.artie.user.application.service;

import static com.yapp.artie.common.UserTestData.defaultUser;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;

import com.yapp.artie.common.BaseUserUnitTest;
import com.yapp.artie.user.application.port.out.GenerateTestTokenPort;
import com.yapp.artie.user.domain.User;
import com.yapp.artie.user.domain.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GetTestUserTokenServiceTest extends BaseUserUnitTest {

  private final GenerateTestTokenPort generateTestTokenPort = Mockito.mock(
      GenerateTestTokenPort.class);

  private final GetTestUserTokenService getTestUserTokenService = new GetTestUserTokenService(
      loadUserPort, generateTestTokenPort);

  @Test
  void loadTestUserToken_사용자를_찾을_수_없으면_예외를_발생한다() {
    givenUserFindWillFail();
    assertThatThrownBy(() -> getTestUserTokenService.loadTestUserToken(1L))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void loadTestUserToken_사용자를_조회하여_토큰을_발급한다() {
    User user = defaultUser().build();
    givenUserByReference(user);

    getTestUserTokenService.loadTestUserToken(1L);
    then(generateTestTokenPort)
        .should()
        .generateTestToken(eq(user.getUid()));
  }
}