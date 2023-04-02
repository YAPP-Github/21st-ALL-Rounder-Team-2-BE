package com.yapp.artie.domain.user.application.service;

import com.yapp.artie.domain.user.application.port.in.GetUserQuery;
import com.yapp.artie.domain.user.application.port.out.LoadUserPort;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.global.common.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetUserService implements GetUserQuery {

  private final LoadUserPort loadUserPort;

  @Override
  public User loadUserById(Long userId) {
    return loadUserPort.loadById(userId);
  }
}
