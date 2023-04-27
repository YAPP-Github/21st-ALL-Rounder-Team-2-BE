package com.yapp.artie.domain.notice.application.service;

import com.yapp.artie.domain.notice.application.port.in.GetNoticeDetailQuery;
import com.yapp.artie.domain.notice.application.port.in.GetNoticeDetailResponse;
import com.yapp.artie.domain.notice.application.port.out.LoadNoticePort;
import com.yapp.artie.domain.notice.domain.Notice;
import com.yapp.artie.global.common.annotation.UseCase;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional(readOnly = true)
@AllArgsConstructor
class GetNoticeDetailService implements GetNoticeDetailQuery {

  private final LoadNoticePort loadNoticePort;

  @Override
  public GetNoticeDetailResponse loadNoticeDetail(Long id) {
    Notice notice = loadNoticePort.loadNoticeDetail(id);
    return new GetNoticeDetailResponse(
        notice.getId(),
        notice.getCreatedAt(),
        notice.getTitle(),
        notice.getContents()
    );
  }
}
