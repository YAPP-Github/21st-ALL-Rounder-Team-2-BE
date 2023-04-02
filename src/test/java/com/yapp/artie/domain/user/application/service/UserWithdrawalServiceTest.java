package com.yapp.artie.domain.user.application.service;

import static com.yapp.artie.common.UserTestData.defaultUser;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.yapp.artie.domain.archive.repository.CategoryRepository;
import com.yapp.artie.domain.user.application.port.exception.UserNotFoundException;
import com.yapp.artie.domain.user.application.port.out.DeleteExternalUserPort;
import com.yapp.artie.domain.user.application.port.out.DeleteUserPort;
import com.yapp.artie.domain.user.application.port.out.LoadUserPort;
import com.yapp.artie.domain.user.domain.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserWithdrawalServiceTest {

  private final CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
  private final DeleteExternalUserPort deleteExternalUserPort = Mockito.mock(
      DeleteExternalUserPort.class);
  private final LoadUserPort loadUserPort = Mockito.mock(LoadUserPort.class);
  private final DeleteUserPort deleteUserPort = Mockito.mock(DeleteUserPort.class);

  private final UserWithdrawalService userWithdrawalService = new UserWithdrawalService(
      deleteExternalUserPort, categoryRepository, loadUserPort, deleteUserPort);

  @Test
  void delete_사용자를_찾을_수_없으면_예외를_발생한다() {
    givenUserFindWillFail();
    assertThatThrownBy(() -> userWithdrawalService.delete(1L))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void delete_사용자_삭제_요청을_받으면_firebase에서_삭제되도록_요청한다() {
    givenUser();
    userWithdrawalService.delete(1L);
    then(deleteExternalUserPort)
        .should()
        .delete(any());
  }

  @Test
  void delete_사용자_삭제_요청을_받으면_사용자의_모든_데이터를_삭제한다() {
    givenUser();
    userWithdrawalService.delete(1L);
    then(categoryRepository)
        .should()
        .deleteAllByUser(any());
  }

  @Test
  void delete_데이터베이스에서_사용자를_삭제하도록_요청한다() {
    User user = defaultUser().build();
    givenUserByReference(user);
    userWithdrawalService.delete(1L);
    then(deleteUserPort)
        .should()
        .delete(eq(user));
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