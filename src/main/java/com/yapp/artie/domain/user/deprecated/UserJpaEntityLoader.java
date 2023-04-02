package com.yapp.artie.domain.user.deprecated;

import com.yapp.artie.domain.user.adapter.out.persistence.UserJpaEntity;
import com.yapp.artie.domain.user.adapter.out.persistence.UserRepository;
import com.yapp.artie.domain.user.application.port.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class UserJpaEntityLoader implements LoadUserJpaEntityApi {

  private final UserRepository userRepository;

  @Override
  public UserJpaEntity findById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(UserNotFoundException::new);
  }
}
