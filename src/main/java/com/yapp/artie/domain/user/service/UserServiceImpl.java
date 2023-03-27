package com.yapp.artie.domain.user.service;

import static org.springframework.security.core.userdetails.User.builder;

import com.yapp.artie.domain.archive.repository.CategoryRepository;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.dto.response.CreateUserResponseDto;
import com.yapp.artie.domain.user.exception.UserNotFoundException;
import com.yapp.artie.domain.user.repository.UserRepository;
import com.yapp.artie.global.authentication.JwtService;
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
  private final CategoryRepository categoryRepository;
  private final JwtService jwtService;
  private final RegisterUserService registerUserService;

  @Override
  public Optional<User> findByUid(String uid) {
    return userRepository.findByUid(uid);
  }

  @Override
  public User findById(Long id) {
    return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
  }

  @Override
  public CreateUserResponseDto register(String uid, String username, String picture) {
    return registerUserService.register(uid, username, picture);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    User user = findById(id);
    jwtService.withdraw(user.getUid());

    categoryRepository.deleteAllByUser(user);
    userRepository.delete(user);
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    User user = userRepository.findByUid(username)
        .orElseThrow(UserNotFoundException::new);

    return builder()
        .username(String.valueOf(user.getId()))
        .password(user.getUid())
        .authorities("user")
        .build();
  }

  @Override
  @Transactional
  public void updateUserName(Long userId, String name) {
    User user = findById(userId);
    user.setName(name);
  }
}
