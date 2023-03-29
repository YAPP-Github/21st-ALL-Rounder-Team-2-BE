package com.yapp.artie.domain.user.application.service;

import com.yapp.artie.domain.user.adapter.out.persistence.UserJpaEntity;
import com.yapp.artie.domain.user.application.port.in.RegisterUserUseCase;
import com.yapp.artie.domain.user.dto.response.CreateUserResponseDto;
import com.yapp.artie.domain.user.adapter.out.persistence.UserRepository;
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

    final Optional<UserJpaEntity> user = userRepository.findByUid(uid);
    return user.map(entity -> new CreateUserResponseDto(entity.getId()))
        .orElseGet(() -> new CreateUserResponseDto(userRepository
            .save(UserJpaEntity.create(uid, username, picture))
            .getId()));
  }
}