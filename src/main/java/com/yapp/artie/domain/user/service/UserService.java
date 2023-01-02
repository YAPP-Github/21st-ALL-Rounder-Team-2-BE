package com.yapp.artie.domain.user.service;

import static org.springframework.security.core.userdetails.User.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.dto.response.CreateUserResponseDto;
import com.yapp.artie.domain.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

  @Transactional
  public CreateUserResponseDto register(String uid, String username) {
    User user = User.create(uid, username);
    userRepository.save(user);
    return new CreateUserResponseDto(user.getId());
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUid(username)
        .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 유저입니다."));

    return builder()
        .username(String.valueOf(user.getId()))
        .build();
  }
}
