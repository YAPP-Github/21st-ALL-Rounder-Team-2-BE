package com.yapp.artie.domain.user.application.port.in;

public interface RenameUserUseCase {

  void rename(Long userId, String name);
}
