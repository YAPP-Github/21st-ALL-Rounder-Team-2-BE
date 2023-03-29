package com.yapp.artie.domain.user.application.service;

import static com.yapp.artie.common.UserTestData.defaultUser;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.yapp.artie.domain.archive.repository.CategoryRepository;
import com.yapp.artie.domain.user.domain.UserJpaEntity;
import com.yapp.artie.domain.user.exception.UserNotFoundException;
import com.yapp.artie.domain.user.repository.UserRepository;
import com.yapp.artie.global.authentication.JwtService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserWithdrawalServiceTest {

  private final UserRepository userRepository = Mockito.mock(UserRepository.class);
  private final CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
  private final JwtService jwtService = Mockito.mock(JwtService.class);
  private final UserWithdrawalService userWithdrawalService = new UserWithdrawalService(
      jwtService, categoryRepository, userRepository);

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
    then(jwtService)
        .should()
        .withdraw(any());
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
    UserJpaEntity user = defaultUser().build();
    givenUserByReference(user);
    userWithdrawalService.delete(1L);
    then(userRepository)
        .should()
        .delete(eq(user));
  }

  private void givenUserByReference(UserJpaEntity user) {
    given(userRepository.findById(any()))
        .willReturn(Optional.ofNullable(user));
  }

  private void givenUser() {
    given(userRepository.findById(any()))
        .willReturn(Optional.ofNullable(defaultUser().build()));
  }

  private void givenUserFindWillFail() {
    given(userRepository.findById(any()))
        .willThrow(UserNotFoundException.class);
  }
}