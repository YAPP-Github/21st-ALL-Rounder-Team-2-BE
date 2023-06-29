package com.yapp.artie.notice.application.service;

import com.yapp.artie.global.common.annotation.UseCase;
import com.yapp.artie.notice.application.port.in.GetNoticeDetailResponse;
import com.yapp.artie.notice.application.port.in.GetNoticeListQuery;
import com.yapp.artie.notice.application.port.out.LoadNoticePort;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional(readOnly = true)
@AllArgsConstructor
class GetNoticeListService implements GetNoticeListQuery {

  private final LoadNoticePort loadNoticePort;

  @Override
  public List<GetNoticeDetailResponse> loadNoticeList() {
    return loadNoticePort.loadNoticeList().stream()
        .map(eachNotice ->
            new GetNoticeDetailResponse(
                eachNotice.getId(),
                eachNotice.getCreatedAt(),
                eachNotice.getTitle(),
                eachNotice.getContents()
            )).collect(Collectors.toList());
  }
}
