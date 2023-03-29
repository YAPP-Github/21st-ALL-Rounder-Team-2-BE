package com.yapp.artie.domain.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.yapp.artie.domain.user.adapter.out.persistence.UserJpaEntity;
import com.yapp.artie.domain.user.adapter.out.persistence.UserRepository;
import com.yapp.artie.domain.user.domain.UserTest;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTest {

  @Autowired
  EntityManager em;

  @Autowired
  UserRepository userRepository;

  @Test
  @DisplayName("유저 삭제")
  void delete() {
    UserJpaEntity user = userRepository.save(UserTest.TEST_SAVED_USER);
    em.clear();

    userRepository.delete(user);
    em.flush();

    assertThat(userRepository.findById(user.getId()).isPresent()).isFalse();
  }

}
