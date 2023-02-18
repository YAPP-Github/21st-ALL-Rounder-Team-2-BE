package com.yapp.artie.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.repository.UserRepository;

import com.yapp.artie.domain.user.dto.response.CreateUserResponseDto;
import com.yapp.artie.domain.user.exception.UserNotFoundException;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
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

  @Test
  public void updateUserName() {
    User user = userRepository.save(User.create("sample-uid", "sample-name", "sample-picture"));

    userService.updateUserName(user.getId(), "sample-name-2");

    assertThat(em.find(User.class, user.getId()).getName()).isEqualTo("sample-name-2");
  }

  @Test
  public void delete_사용자_삭제시_테이블에서_제거된다() throws Exception {
    CreateUserResponseDto dto = userService.register("123", "le2sky", null);
    userService.delete(dto.id);

    assertThatThrownBy(() -> {
      userService.delete(dto.id);
    }).isInstanceOf(UserNotFoundException.class);
  }
}