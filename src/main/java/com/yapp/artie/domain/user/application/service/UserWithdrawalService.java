package com.yapp.artie.domain.user.application.service;

import com.yapp.artie.domain.archive.repository.CategoryRepository;
import com.yapp.artie.domain.user.application.port.in.UserWithdrawalUseCase;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.exception.UserNotFoundException;
import com.yapp.artie.domain.user.repository.UserRepository;
import com.yapp.artie.global.annotation.UseCase;
import com.yapp.artie.global.authentication.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Transactional
@RequiredArgsConstructor
public class UserWithdrawalService implements UserWithdrawalUseCase {

  private final JwtService jwtService;
  private final CategoryRepository categoryRepository;
  private final UserRepository userRepository;

  @Override
  public void delete(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(UserNotFoundException::new);

    jwtService.withdraw(user.getUid());
    categoryRepository.deleteAllByUser(user);
    userRepository.delete(user);
  }

}