package com.yapp.artie.domain.user.application.service;

import com.yapp.artie.domain.user.adapter.out.persistence.UserJpaEntity;
import com.yapp.artie.domain.user.adapter.out.persistence.UserRepository;
import com.yapp.artie.domain.user.application.port.in.RenameUserUseCase;
import com.yapp.artie.domain.user.exception.UserNotFoundException;
import com.yapp.artie.global.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional
@RequiredArgsConstructor
public class RenameUserService implements RenameUserUseCase {

  private final UserRepository userRepository;

  @Override
  public void rename(Long userId, String name) {
    UserJpaEntity user = userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);

    user.setName(name);
  }
}