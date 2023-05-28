package com.yapp.artie.domain.user.application.port.in.command;

public interface RenameUserUseCase {

  void rename(Long userId, String name);
}
