package com.yapp.artie.notice.application.port.in;

import java.util.List;

public interface GetNoticeListQuery {

  List<GetNoticeDetailResponse> loadNoticeList();
}
