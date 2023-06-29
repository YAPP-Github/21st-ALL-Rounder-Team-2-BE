package com.yapp.artie.user.application.service;

import com.yapp.artie.global.common.annotation.UseCase;
import com.yapp.artie.user.application.port.in.command.RenameUserUseCase;
import com.yapp.artie.user.application.port.out.LoadUserPort;
import com.yapp.artie.user.application.port.out.UpdateUserStatePort;
import com.yapp.artie.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional
@RequiredArgsConstructor
class RenameUserService implements RenameUserUseCase {

  private final LoadUserPort loadUserPort;
  private final UpdateUserStatePort updateUserStatePort;

  @Override
  public void rename(Long userId, String name) {
    User user = loadUserPort.loadById(userId);
    user.rename(name);

    updateUserStatePort.updateName(user);
  }
}