package com.yapp.artie.domain.user.service;

import com.yapp.artie.domain.user.domain.UserJpaEntity;
import com.yapp.artie.domain.user.dto.response.CreateUserResponseDto;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserUseCase {

  Optional<UserJpaEntity> findByUid(String uid);

  UserJpaEntity findById(Long id);

  CreateUserResponseDto register(String uid, String username, String picture);

  void delete(Long id);

  UserDetails loadUserByUsername(String username);

  void updateUserName(Long userId, String name);
}
