package com.yapp.artie.domain.notice.application.port.out;

import com.yapp.artie.domain.notice.application.port.in.GetNoticeDetailResponse;
import java.util.List;

public interface LoadNoticePort {

  List<GetNoticeDetailResponse> loadNoticeList();

  GetNoticeDetailResponse loadNoticeDetail(Long id);
}
