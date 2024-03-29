package com.yapp.artie.user.application.service;

import com.yapp.artie.global.common.annotation.UseCase;
import com.yapp.artie.user.application.port.in.query.GetTestUserTokenQuery;
import com.yapp.artie.user.application.port.out.GenerateTestTokenPort;
import com.yapp.artie.user.application.port.out.LoadUserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
class GetTestUserTokenService implements GetTestUserTokenQuery {

  private final LoadUserPort loadUserPort;
  private final GenerateTestTokenPort generateTestTokenPort;

  @Override
  public String loadTestUserToken(Long userId) {
    String uid = loadUserPort.loadById(userId).getUid();
    return generateTestTokenPort.generateTestToken(uid);
  }
}
