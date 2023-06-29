package com.yapp.artie.user.application.port.in.command;

public interface RenameUserUseCase {

  void rename(Long userId, String name);
}
