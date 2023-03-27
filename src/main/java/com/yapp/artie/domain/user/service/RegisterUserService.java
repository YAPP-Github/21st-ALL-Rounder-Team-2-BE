package com.yapp.artie.domain.user.service;

import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.dto.response.CreateUserResponseDto;
import com.yapp.artie.domain.user.repository.UserRepository;
import com.yapp.artie.global.annotation.UseCase;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {

  private final UserRepository userRepository;

  @Override
  public CreateUserResponseDto register(final String uid, final String username,
      final String picture) {

    final Optional<User> user = userRepository.findByUid(uid);
    return user.map(entity -> new CreateUserResponseDto(entity.getId()))
        .orElseGet(() -> new CreateUserResponseDto(userRepository
            .save(User.create(uid, username, picture))
            .getId()));
  }
}