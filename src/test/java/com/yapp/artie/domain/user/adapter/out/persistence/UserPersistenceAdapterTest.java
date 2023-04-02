package com.yapp.artie.domain.user.adapter.out.persistence;

import static com.yapp.artie.common.UserTestData.defaultUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.yapp.artie.domain.user.application.port.exception.UserNotFoundException;
import com.yapp.artie.domain.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Import({UserPersistenceAdapter.class, UserMapper.class})
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UserPersistenceAdapterTest {

  @Autowired
  private UserPersistenceAdapter adapterUnderTest;

  @Autowired
  private UserRepository userRepository;

  @Test
  @Sql("UserPersistenceAdapterTest.sql")
  void loadById_id를_이용해서_사용자_조회() {
    User user = adapterUnderTest.loadById(1L);
    assertThat(user.getName()).isEqualTo("이하늘");
  }

  @Test
  void loadById_사용자를_찾을_수_없으면_예외를_발생한다() {
    assertThatThrownBy(() -> {
      adapterUnderTest.loadById(1L);
    }).isInstanceOf(UserNotFoundException.class);
  }

  @Test
  @Sql("UserPersistenceAdapterTest.sql")
  void loadById_uid를_이용해서_사용자_조회() {
    User user = adapterUnderTest.loadByUid("mock-01");
    assertThat(user.getName()).isEqualTo("이하늘");
  }

  @Test
  void loadByUid_사용자를_찾을_수_없으면_예외를_발생한다() {
    assertThatThrownBy(() -> {
      adapterUnderTest.loadByUid("no-user-uid");
    }).isInstanceOf(UserNotFoundException.class);
  }

  @Test
  @Sql("UserPersistenceAdapterTest.sql")
  void delete_사용자를_데이터베이스에서_삭제한다() {
    adapterUnderTest.delete(adapterUnderTest.loadById(1L));

    assertThat(userRepository.count()).isEqualTo(5);
    assertThatThrownBy(() -> {
      adapterUnderTest.loadById(1L);
    }).isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void delete_사용자를_찾을_수_없으면_예외를_발생한다() {
    assertThatThrownBy(() -> {
      adapterUnderTest.delete(adapterUnderTest.loadById(1L));
    }).isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void save_사용자를_데이터베이스에_저장한다() {
    adapterUnderTest.save(defaultUser().withId(null).build());
    assertThat(userRepository.count()).isEqualTo(1);
  }

  @Test
  @Sql("UserPersistenceAdapterTest.sql")
  void updateName_사용자의_이름을_변경한다() {
    User user = adapterUnderTest.loadById(1L);
    user.rename("tomcat");

    adapterUnderTest.updateName(user);

    String actual = userRepository.findById(1L)
        .orElseThrow()
        .getName();
    assertThat(actual).isEqualTo("tomcat");
  }

  @Test
  void updateName_사용자를_찾을_수_없으면_예외를_발생한다() {
    assertThatThrownBy(() -> {
      adapterUnderTest.updateName(defaultUser().build());
    }).isInstanceOf(UserNotFoundException.class);
  }
}