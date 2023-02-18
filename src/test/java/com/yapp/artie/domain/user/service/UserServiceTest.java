package com.yapp.artie.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.repository.UserRepository;

import com.yapp.artie.domain.user.dto.response.CreateUserResponseDto;
import com.yapp.artie.domain.user.exception.UserNotFoundException;
import com.yapp.artie.global.authentication.JwtService;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class UserServiceTest {

  @Autowired
  EntityManager em;

  @Autowired
  UserRepository userRepository;

  @Autowired
  UserService userService;

  JwtService mockJwtService = Mockito.mock(JwtService.class);

  @Test
  public void updateUserName() {
    User user = userRepository.save(User.create("sample-uid", "sample-name", "sample-picture"));

    userService.updateUserName(user.getId(), "sample-name-2");

    assertThat(em.find(User.class, user.getId()).getName()).isEqualTo("sample-name-2");
  }

  @Test
  public void delete_사용자_삭제시_테이블에서_제거된다() throws Exception {
    CreateUserResponseDto dto = userService.register("123", "le2sky", null);
    userService.delete(dto.id, mockJwtService);

    assertThatThrownBy(() -> {
      userService.delete(dto.id, mockJwtService);
    }).isInstanceOf(UserNotFoundException.class);
  }

  @Test
  public void delete_사용자_삭제시_파이어베이스에_사용자_삭제_요청() throws Exception {
    String expectedUid = "123";
    CreateUserResponseDto dto = userService.register(expectedUid, "le2sky", null);
    userService.delete(dto.id, mockJwtService);

    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    BDDMockito.then(mockJwtService)
        .should()
        .withdraw(captor.capture());

    assertThat(captor.getValue()).isEqualTo(expectedUid);
  }
}