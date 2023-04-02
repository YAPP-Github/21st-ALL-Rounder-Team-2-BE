package com.yapp.artie.domain.user.application.service;

import com.yapp.artie.domain.user.application.port.in.RegisterUserResponse;
import com.yapp.artie.domain.user.application.port.in.RegisterUserUseCase;
import com.yapp.artie.domain.user.application.port.out.LoadUserPort;
import com.yapp.artie.domain.user.application.port.out.SaveUserPort;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.global.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {

  private final LoadUserPort loadUserPort;
  private final SaveUserPort saveUserPort;

  @Override
  public RegisterUserResponse register(final String uid, final String username,
      final String picture) {
    try {
      return new RegisterUserResponse(getIdIfExists(uid));
    } catch (UserNotFoundException notFoundException) {
      return new RegisterUserResponse(saveUser(User.withoutId(uid, picture, username)));
    }
  }

  private Long saveUser(final User user) {
    return saveUserPort.save(user);
  }

  private Long getIdIfExists(final String uid) {
    return loadUserPort.loadByUid(uid).getId();
  }
}