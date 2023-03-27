package com.yapp.artie.domain.user.service;

import com.yapp.artie.domain.user.dto.response.CreateUserResponseDto;

public interface RegisterUserUseCase {

  CreateUserResponseDto register(String uid, String username, String picture);
}
