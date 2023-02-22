package com.yapp.artie.domain.user.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.domain.UserTest;
import com.yapp.artie.domain.user.exception.UserNotFoundException;
import com.yapp.artie.domain.user.repository.UserRepository;
import com.yapp.artie.global.authentication.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private JwtService jwtService;

  @Test
  @DisplayName("유저 닉네임(이름) 변경 테스트 - 존재하지 않는 userId로 조회시 예외처리")
  public void updateUserNameFailTest() throws Exception {
    User user = UserTest.TEST_USER;
    given(userRepository.findById(any())).willThrow(new UserNotFoundException());

    assertThatThrownBy(() -> userService.updateUserName(user.getId(), "new-name")).isInstanceOf(
        UserNotFoundException.class);
  }

  // TODO: delete 서비스 메소드가, jwtService.withDraw를, FirebaseAuth.deleteUser를 잘 호출하는지 확인하는 테스트 추가 필요
}