package com.yapp.artie.common;

import static com.yapp.artie.common.UserTestData.defaultUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.yapp.artie.user.application.port.out.LoadUserPort;
import com.yapp.artie.user.application.port.out.SaveUserPort;
import com.yapp.artie.user.domain.User;
import com.yapp.artie.user.domain.UserNotFoundException;
import org.mockito.Mockito;

abstract public class BaseUserUnitTest {

  protected final LoadUserPort loadUserPort = Mockito.mock(LoadUserPort.class);
  protected final SaveUserPort saveUserPort = Mockito.mock(SaveUserPort.class);

  protected void givenUser() {
    given(loadUserPort.loadById(any()))
        .willReturn(defaultUser().build());
  }

  protected void givenUserFindWillFail() {
    given(loadUserPort.loadById(any()))
        .willThrow(UserNotFoundException.class);
  }

  protected void givenUserByReference(User user) {
    given(loadUserPort.loadById(any()))
        .willReturn(user);
  }

  protected void givenUserByReferenceWithUid(User user) {
    given(loadUserPort.loadByUid(any()))
        .willReturn(user);
  }

  protected void givenSaveWillReturnId(Long id) {
    given(saveUserPort.save(any()))
        .willReturn(id);
  }

  protected void givenUserFindWithUid() {
    given(loadUserPort.loadByUid(any()))
        .willReturn(defaultUser().build());
  }

  protected void givenUserFindWithUidWillFail() {
    given(loadUserPort.loadByUid(any()))
        .willThrow(UserNotFoundException.class);
  }
}
