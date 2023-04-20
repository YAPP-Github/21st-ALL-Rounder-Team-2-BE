package com.yapp.artie.domain.notice.application.port.in;

public interface GetNoticeDetailQuery {

  GetNoticeDetailResponse loadNoticeDetail(Long id);
}
