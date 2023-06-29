package com.yapp.artie.notice.application.port.in;

public interface GetNoticeDetailQuery {

  GetNoticeDetailResponse loadNoticeDetail(Long id);
}
