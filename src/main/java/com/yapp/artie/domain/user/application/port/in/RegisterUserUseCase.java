package com.yapp.artie.domain.user.application.port.in;

public interface RegisterUserUseCase {

  RegisterUserResponse register(String uid, String username, String picture);
}
