package com.yapp.artie.domain.user.application.service;

import com.yapp.artie.domain.user.application.port.in.RenameUserUseCase;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.exception.UserNotFoundException;
import com.yapp.artie.domain.user.repository.UserRepository;
import com.yapp.artie.global.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional
@RequiredArgsConstructor
public class RenameUserService implements RenameUserUseCase {

  private final UserRepository userRepository;

  @Override
  @Transactional
  public void rename(Long userId, String name) {
    User user = userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);

    user.setName(name);
  }
}