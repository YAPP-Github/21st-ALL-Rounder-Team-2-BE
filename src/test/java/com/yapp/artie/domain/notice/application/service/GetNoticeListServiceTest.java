package com.yapp.artie.domain.notice.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.yapp.artie.domain.notice.application.port.in.GetNoticeDetailResponse;
import com.yapp.artie.domain.notice.application.port.out.LoadNoticePort;
import com.yapp.artie.domain.notice.domain.Notice;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GetNoticeListServiceTest {

  private final LoadNoticePort loadNoticePort = Mockito.mock(LoadNoticePort.class);
  private final GetNoticeListService getNoticeListService = new GetNoticeListService(
      loadNoticePort);

  @Test
  void loadNoticeList_모든_공지_조회() {
    List<Notice> notices = Arrays.asList(Notice.create("공지 제목 1", "공지 내용 1"),
        Notice.create("공지 제목 2", null));
    givenNoticeList(notices);

    List<GetNoticeDetailResponse> getNoticeDetailResponses = getNoticeListService.loadNoticeList();

    assertThat(getNoticeDetailResponses.size()).isEqualTo(2);
    assertThat(getNoticeDetailResponses.get(0).getTitle()).isEqualTo("공지 제목 1");
  }

  @Test
  void loadNoticeList_공지가_없을_경우_빈_리스트를_반환한다() {
    givenNoticeList(new ArrayList<>());

    List<GetNoticeDetailResponse> noticeDetailResponses = getNoticeListService.loadNoticeList();

    assertThat(noticeDetailResponses.size()).isEqualTo(0);
  }

  private void givenNoticeList(List<Notice> notices) {
    given(loadNoticePort.loadNoticeList()).willReturn(notices);
  }
}