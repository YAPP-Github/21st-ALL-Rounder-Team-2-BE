package com.yapp.artie.domain.user.service;

import com.yapp.artie.domain.user.adapter.out.persistence.UserJpaEntity;
import com.yapp.artie.domain.user.dto.response.CreateUserResponseDto;

// TODO : Deprecated 애노테이션을 붙이고, LoadUserJpaEntity 어쩌구로 이름 변경 필요
public interface UserUseCase {

  UserJpaEntity findById(Long id);

  CreateUserResponseDto register(String uid, String username, String picture);

  void delete(Long id);

  void updateUserName(Long userId, String name);
}
