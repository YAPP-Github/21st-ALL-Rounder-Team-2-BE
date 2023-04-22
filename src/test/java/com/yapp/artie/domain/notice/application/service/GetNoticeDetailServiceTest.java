package com.yapp.artie.domain.notice.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.yapp.artie.domain.notice.application.port.in.GetNoticeDetailResponse;
import com.yapp.artie.domain.notice.application.port.out.LoadNoticePort;
import com.yapp.artie.domain.notice.domain.Notice;
import com.yapp.artie.domain.notice.domain.NoticeNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GetNoticeDetailServiceTest {

  private final LoadNoticePort loadNoticePort = Mockito.mock(LoadNoticePort.class);
  private final GetNoticeDetailService getNoticeDetailService = new GetNoticeDetailService(
      loadNoticePort);

  @Test
  void loadNoticeDetail_공지_ID를_이용해서_공지_세부_내용을_조회한다() {
    Notice notice = Notice.create("공지 제목 1", "공지 내용 1");
    givenNotice(notice);

    GetNoticeDetailResponse noticeDetailResponse = getNoticeDetailService.loadNoticeDetail(
        notice.getId());

    assertThat(noticeDetailResponse.getId()).isEqualTo(notice.getId());
  }

  @Test
  void loadNoticeDetail_공지_ID를_찾지_못한_경우_예외를_발생한다() {
    givenNoticeByIdWillFail();

    assertThatThrownBy(() -> getNoticeDetailService.loadNoticeDetail(1L)).isInstanceOf(
        NoticeNotFoundException.class);
  }

  private void givenNotice(Notice notice) {
    given(loadNoticePort.loadNoticeDetail(any()))
        .willReturn(notice);
  }

  private void givenNoticeByIdWillFail() {
    given(loadNoticePort.loadNoticeDetail(any()))
        .willThrow(NoticeNotFoundException.class);
  }
}