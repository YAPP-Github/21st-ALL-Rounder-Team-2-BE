package com.yapp.artie.domain.user.application.service;

import static com.yapp.artie.common.UserTestData.defaultUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.yapp.artie.domain.user.application.port.out.LoadUserPort;
import com.yapp.artie.domain.user.domain.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;

class GetUserDetailsServiceTest {

  private final LoadUserPort loadUserPort = Mockito.mock(LoadUserPort.class);
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

  private void givenUserByReferenceWithUid(User user) {
    given(loadUserPort.loadByUid(any()))
        .willReturn(user);
  }
}