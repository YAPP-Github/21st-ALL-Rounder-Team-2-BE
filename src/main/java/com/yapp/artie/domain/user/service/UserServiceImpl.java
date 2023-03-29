package com.yapp.artie.domain.user.service;

import static org.springframework.security.core.userdetails.User.builder;

import com.yapp.artie.domain.user.adapter.out.persistence.UserJpaEntity;
import com.yapp.artie.domain.user.adapter.out.persistence.UserRepository;
import com.yapp.artie.domain.user.application.service.RegisterUserService;
import com.yapp.artie.domain.user.application.service.RenameUserService;
import com.yapp.artie.domain.user.application.service.UserWithdrawalService;
import com.yapp.artie.domain.user.dto.response.CreateUserResponseDto;
import com.yapp.artie.domain.user.exception.UserNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserUseCase {

  private final UserRepository userRepository;
  private final RegisterUserService registerUserService;
  private final RenameUserService renameUserService;
  private final UserWithdrawalService userWithdrawalService;

  @Override
  public Optional<UserJpaEntity> findByUid(String uid) {
    return userRepository.findByUid(uid);
  }

  @Override
  public UserJpaEntity findById(Long id) {
    return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
  }

  @Override
  public CreateUserResponseDto register(String uid, String username, String picture) {
    return registerUserService.register(uid, username, picture);
  }

  @Override
  public void delete(Long id) {
    userWithdrawalService.delete(id);
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    UserJpaEntity user = userRepository.findByUid(username)
        .orElseThrow(UserNotFoundException::new);

    return builder()
        .username(String.valueOf(user.getId()))
        .password(user.getUid())
        .authorities("user")
        .build();
  }

  @Override
  public void updateUserName(Long userId, String name) {
    renameUserService.rename(userId, name);
  }
}
