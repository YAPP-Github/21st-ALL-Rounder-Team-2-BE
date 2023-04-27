package com.yapp.artie.domain.user.application.service;

import com.yapp.artie.domain.user.application.port.in.GetUserJoinDateQuery;
import com.yapp.artie.domain.user.application.port.out.LoadUserPort;
import com.yapp.artie.global.common.annotation.UseCase;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
class GetUserJoinDateService implements GetUserJoinDateQuery {

  private final LoadUserPort loadUserPort;

  @Override
  public LocalDateTime loadUserJoinDate(Long userId) {
    return loadUserPort.loadJoinDateById(userId);
  }
}
