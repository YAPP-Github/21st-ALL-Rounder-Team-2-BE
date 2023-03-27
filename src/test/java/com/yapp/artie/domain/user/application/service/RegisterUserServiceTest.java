package com.yapp.artie.domain.user.application.service;

import static com.yapp.artie.common.UserTestData.defaultUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.yapp.artie.domain.user.dto.response.CreateUserResponseDto;
import com.yapp.artie.domain.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class RegisterUserServiceTest {

  private final UserRepository userRepository = Mockito.mock(UserRepository.class);
  private final RegisterUserService registerUserService = new RegisterUserService(userRepository);

  @Test
  void register_이미_등록된_사용자라면_ID를_그대로_반환한다() {
    givenUser();
    CreateUserResponseDto actual = registerUserService.register("1", "test", null);
    assertThat(actual.getId()).isEqualTo(1L);
  }

  @Test
  void register_이미_등록된_사용자라면_저장소에_저장하지_않는다() {
    givenUser();
    registerUserService.register("1", "test", null);
    then(userRepository)
        .should(never())
        .save(any());
  }

  @Test
  void register_신규_사용자라면_새로운_id를_부여받는다() {
    Long expectedId = 2L;
    givenSaveWillReturnUserWithId(expectedId);
    CreateUserResponseDto actual = registerUserService.register("1", "test", null);
    assertThat(actual.getId()).isEqualTo(expectedId);
    then(userRepository)
        .should()
        .save(any());
  }

  private void givenUser() {
    given(userRepository.findByUid(any()))
        .willReturn(Optional.ofNullable(defaultUser().build()));
  }

  private void givenSaveWillReturnUserWithId(Long id) {
    given(userRepository.save(any()))
        .willReturn(defaultUser()
            .withId(id)
            .build());
  }
}