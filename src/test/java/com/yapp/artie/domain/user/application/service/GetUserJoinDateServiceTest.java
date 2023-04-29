package com.yapp.artie.domain.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.yapp.artie.domain.user.application.port.out.LoadUserPort;
import com.yapp.artie.domain.user.domain.UserNotFoundException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GetUserJoinDateServiceTest {

  protected final LoadUserPort loadUserPort = Mockito.mock(LoadUserPort.class);

  private final GetUserJoinDateService getUserJoinDateService = new GetUserJoinDateService(
      loadUserPort);

  @Test
  void loadUserJoinDate_사용자를_찾을_수_없으면_예외를_발생한다() {
    givenUserJoinDateFindWillFail();
    assertThatThrownBy(() -> getUserJoinDateService.loadUserJoinDate(1L))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void loadUserJoinDate_사용자를_조회한다() {
    LocalDateTime now = LocalDateTime.now();
    givenUserByDate(now);
    LocalDateTime joinDate = getUserJoinDateService.loadUserJoinDate(1L);
    assertThat(joinDate).isEqualTo(now);
  }

  private void givenUserJoinDateFindWillFail() {
    given(loadUserPort.loadJoinDateById(any()))
        .willThrow(UserNotFoundException.class);
  }

  private void givenUserByDate(LocalDateTime date) {
    given(loadUserPort.loadJoinDateById(any()))
        .willReturn(date);
  }
}