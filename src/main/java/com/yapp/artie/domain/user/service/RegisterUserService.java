package com.yapp.artie.domain.user.service;

import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.dto.response.CreateUserResponseDto;
import com.yapp.artie.domain.user.repository.UserRepository;
import com.yapp.artie.global.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegisterUserService {

  private final UserRepository userRepository;

  @Transactional
  public CreateUserResponseDto register(String uid, String username, String picture) {
    User user = userRepository.findByUid(uid)
        .orElse(User.create(uid, username, picture));

    if (user.getId() == null) {
      userRepository.save(user);
    }

    return new CreateUserResponseDto(user.getId());
  }
}