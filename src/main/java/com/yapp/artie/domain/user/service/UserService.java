package com.yapp.artie.domain.user.service;

import static org.springframework.security.core.userdetails.User.builder;

import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.dto.response.CreateUserResponseDto;
import com.yapp.artie.domain.user.exception.UserNotFoundException;
import com.yapp.artie.domain.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  public Optional<User> findByUid(String uid) {
    return userRepository.findByUid(uid);
  }

  public User findById(Long id) {
    return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
  }

  @Transactional
  public CreateUserResponseDto register(String uid, String username, String picture) {
    User user = findByUid(uid)
        .orElse(User.create(uid, username, picture));

    if (user.getId() == null) {
      userRepository.save(user);
    }

    return new CreateUserResponseDto(user.getId());
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
}
