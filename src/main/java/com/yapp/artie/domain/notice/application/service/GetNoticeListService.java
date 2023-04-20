package com.yapp.artie.domain.notice.application.service;

import com.yapp.artie.domain.notice.application.port.in.GetNoticeDetailResponse;
import com.yapp.artie.domain.notice.application.port.in.GetNoticeListQuery;
import com.yapp.artie.global.common.annotation.UseCase;
import java.util.List;

@UseCase
public class GetNoticeListService implements GetNoticeListQuery {

  @Override
  public List<GetNoticeDetailResponse> loadNoticeList() {
    return null;
  }
}
