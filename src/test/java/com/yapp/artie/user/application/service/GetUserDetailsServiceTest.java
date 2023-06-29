package com.yapp.artie.user.application.service;

import static com.yapp.artie.common.UserTestData.defaultUser;
import static org.assertj.core.api.Assertions.assertThat;

import com.yapp.artie.common.BaseUserUnitTest;
import com.yapp.artie.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

class GetUserDetailsServiceTest extends BaseUserUnitTest {

  private final GetUserDetailsService getUserDetailsService = new GetUserDetailsService(
      loadUserPort);

  @Test
  void loadUserByUsername_사용자를_조회했을_때_반환되는_객체의_ID는_엔티티의_식별자와_같다() {
    User user = defaultUser().build();
    givenUserByReferenceWithUid(user);
    UserDetails actual = getUserDetailsService.loadUserByUsername(user.getUid());
    assertThat(actual.getUsername()).isEqualTo(user.getId().toString());
  }

  @Test
  void loadUserByUsername_사용자를_조회했을_때_반환되는_객체의_PW는_엔티티의_UID와_같다() {
    User user = defaultUser().build();
    givenUserByReferenceWithUid(user);
    UserDetails actual = getUserDetailsService.loadUserByUsername(user.getUid());
    assertThat(actual.getPassword()).isEqualTo(user.getUid());
  }
}