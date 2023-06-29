package com.yapp.artie.user.application.service;

import com.yapp.artie.global.common.annotation.UseCase;
import com.yapp.artie.user.application.port.in.command.RegisterUserUseCase;
import com.yapp.artie.user.application.port.in.response.RegisterUserResponse;
import com.yapp.artie.user.application.port.out.LoadUserPort;
import com.yapp.artie.user.application.port.out.SaveUserPort;
import com.yapp.artie.user.domain.User;
import com.yapp.artie.user.domain.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional
@RequiredArgsConstructor
class RegisterUserService implements RegisterUserUseCase {

  private final LoadUserPort loadUserPort;
  private final SaveUserPort saveUserPort;

  @Override
  public RegisterUserResponse register(String uid, String username,
      final String picture) {
    try {
      return new RegisterUserResponse(getIdIfExists(uid));
    } catch (UserNotFoundException notFoundException) {
      return new RegisterUserResponse(saveUser(User.withoutId(uid, picture, username)));
    }
  }

  private Long saveUser(User user) {
    return saveUserPort.save(user);
  }

  private Long getIdIfExists(String uid) {
    return loadUserPort.loadByUid(uid).getId();
  }
}