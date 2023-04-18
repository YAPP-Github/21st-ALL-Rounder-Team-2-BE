package com.yapp.artie.domain.user.application.service;

import static com.yapp.artie.common.UserTestData.defaultUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.yapp.artie.domain.archive.service.ExhibitService;
import com.yapp.artie.domain.user.application.port.in.GetUserThumbnailResponse;
import com.yapp.artie.domain.user.application.port.out.LoadUserPort;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.domain.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GetUserThumbnailServiceTest {

  private final LoadUserPort loadUserPort = Mockito.mock(LoadUserPort.class);
  private final ExhibitService exhibitService = Mockito.mock(ExhibitService.class);
  private final GetUserThumbnailService getUserThumbnailService = new GetUserThumbnailService(
      loadUserPort, exhibitService);

  @Test
  void loadUserThumbnailById_사용자를_찾을_수_없으면_예외를_발생한다() {
    givenUserFindWillFail();
    assertThatThrownBy(() -> getUserThumbnailService.loadUserThumbnailById(1L))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void loadUserThumbnailById_사용자의_닉네임과_전시개수를_반환한다() {
    User user = defaultUser()
        .withName("tomcat")
        .build();
    givenUserByReference(user);
    givenExhibitCountWillReturnBy(5);

    GetUserThumbnailResponse userThumbnail = getUserThumbnailService.loadUserThumbnailById(
        user.getId());
    assertThat(userThumbnail.getExhibitCount()).isEqualTo(5);
    assertThat(userThumbnail.getName()).isEqualTo("tomcat");
  }

  private void givenExhibitCountWillReturnBy(int count) {
    given(exhibitService.getExhibitCount(any()))
        .willReturn(count);
  }

  private void givenUserByReference(User user) {
    given(loadUserPort.loadById(any()))
        .willReturn(user);
  }

  private void givenUserFindWillFail() {
    given(loadUserPort.loadById(any()))
        .willThrow(UserNotFoundException.class);
  }
}