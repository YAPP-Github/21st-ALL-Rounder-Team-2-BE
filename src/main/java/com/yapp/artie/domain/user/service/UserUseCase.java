package com.yapp.artie.domain.user.service;

import com.yapp.artie.domain.user.adapter.out.persistence.UserJpaEntity;
import com.yapp.artie.domain.user.dto.response.CreateUserResponseDto;

public interface UserUseCase {

  UserJpaEntity findById(Long id);

  CreateUserResponseDto register(String uid, String username, String picture);

  void delete(Long id);

  void updateUserName(Long userId, String name);
}
