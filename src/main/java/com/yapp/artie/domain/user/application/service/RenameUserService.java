package com.yapp.artie.domain.user.application.service;

import com.yapp.artie.domain.user.application.port.in.RenameUserUseCase;
import com.yapp.artie.domain.user.application.port.out.LoadUserPort;
import com.yapp.artie.domain.user.application.port.out.UpdateUserStatePort;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.global.common.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional
@RequiredArgsConstructor
public class RenameUserService implements RenameUserUseCase {

  private final LoadUserPort loadUserPort;
  private final UpdateUserStatePort updateUserStatePort;

  @Override
  public void rename(Long userId, String name) {
    User user = loadUserPort.loadById(userId);
    user.rename(name);

    updateUserStatePort.updateName(user);
  }
}