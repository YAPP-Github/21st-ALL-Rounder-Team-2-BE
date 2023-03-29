package com.yapp.artie.domain.user.application.service;

import static com.yapp.artie.common.UserTestData.defaultUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.yapp.artie.domain.user.adapter.out.persistence.UserJpaEntity;
import com.yapp.artie.domain.user.exception.UserNotFoundException;
import com.yapp.artie.domain.user.adapter.out.persistence.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RenameUserServiceTest {

  private final UserRepository userRepository = Mockito.mock(UserRepository.class);
  private final RenameUserService renameUserService = new RenameUserService(userRepository);

  @Test
  void rename_사용자를_찾을_수_없으면_예외를_발생한다() {
    givenUserFindWillFail();
    assertThatThrownBy(() -> renameUserService.rename(1L, "test"))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void rename_주어진_이름으로_사용자의_이름이_변경된다() {
    UserJpaEntity user = defaultUser().build();
    givenUserByReference(user);

    String beforeName = userRepository
        .findById(1L)
        .orElseThrow()
        .getName();

    String expectedName = "tomcat";
    renameUserService.rename(1L, expectedName);

    assertThat(user.getName()).isEqualTo(expectedName);
    assertThat(user.getName()).isNotEqualTo(beforeName);
  }


  private void givenUserByReference(UserJpaEntity user) {
    given(userRepository.findById(any()))
        .willReturn(Optional.ofNullable(user));
  }

  private void givenUserFindWillFail() {
    given(userRepository.findById(any()))
        .willThrow(UserNotFoundException.class);
  }


}