package com.yapp.artie.user.application.service;

import com.yapp.artie.global.common.annotation.UseCase;
import com.yapp.artie.user.application.port.in.query.GetUserQuery;
import com.yapp.artie.user.application.port.out.LoadUserPort;
import com.yapp.artie.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
class GetUserService implements GetUserQuery {

  private final LoadUserPort loadUserPort;

  @Override
  public User loadUserById(Long userId) {
    return loadUserPort.loadById(userId);
  }
}
