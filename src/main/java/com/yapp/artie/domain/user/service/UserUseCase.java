package com.yapp.artie.domain.user.service;

import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.dto.response.CreateUserResponseDto;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

public interface UserUseCase {

  Optional<User> findByUid(String uid);

  User findById(Long id);

  CreateUserResponseDto register(String uid, String username, String picture);

  void delete(Long id);

  UserDetails loadUserByUsername(String username);

  void updateUserName(Long userId, String name);
}
