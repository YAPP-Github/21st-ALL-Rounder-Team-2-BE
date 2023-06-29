package com.yapp.artie.user.application.service;

import static org.springframework.security.core.userdetails.User.builder;

import com.yapp.artie.global.common.annotation.UseCase;
import com.yapp.artie.user.application.port.in.query.GetUserDetailsQuery;
import com.yapp.artie.user.application.port.out.LoadUserPort;
import com.yapp.artie.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
class GetUserDetailsService implements GetUserDetailsQuery {

  private final LoadUserPort loadUserPort;

  @Override
  public UserDetails loadUserByUsername(String uid) {
    User user = loadUserPort.loadByUid(uid);

    return builder()
        .username(String.valueOf(user.getId()))
        .password(user.getUid())
        .authorities("user")
        .build();
  }
}
