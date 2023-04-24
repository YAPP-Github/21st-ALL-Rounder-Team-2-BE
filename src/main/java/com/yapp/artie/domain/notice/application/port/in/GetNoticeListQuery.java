package com.yapp.artie.domain.notice.application.port.in;

import java.util.List;

public interface GetNoticeListQuery {

  List<GetNoticeDetailResponse> loadNoticeList();
}
