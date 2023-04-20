package com.yapp.artie.domain.notice.application.service;

import com.yapp.artie.domain.notice.application.port.in.GetNoticeDetailQuery;
import com.yapp.artie.domain.notice.application.port.in.GetNoticeDetailResponse;
import com.yapp.artie.global.common.annotation.UseCase;

@UseCase
public class GetNoticeDetailService implements GetNoticeDetailQuery {

  @Override
  public GetNoticeDetailResponse loadNoticeDetail(Long id) {
    return null;
  }
}
