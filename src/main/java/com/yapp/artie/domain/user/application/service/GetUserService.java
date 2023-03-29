package com.yapp.artie.domain.user.application.service;

import static org.springframework.security.core.userdetails.User.builder;

import com.yapp.artie.domain.user.application.port.in.GetUserQuery;
import com.yapp.artie.domain.user.application.port.out.LoadUserPort;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.global.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
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
