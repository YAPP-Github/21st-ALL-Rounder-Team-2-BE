package com.yapp.artie.domain.user.application.port.in.command;

import com.yapp.artie.domain.user.application.port.in.response.RegisterUserResponse;

public interface RegisterUserUseCase {

  RegisterUserResponse register(String uid, String username, String picture);
}
