package com.yapp.artie.global.deprecated;

import com.yapp.artie.domain.user.adapter.out.persistence.UserJpaEntity;
import com.yapp.artie.domain.user.application.service.UserNotFoundException;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class UserJpaEntityLoader implements LoadUserJpaEntityApi {

  private final EntityManager em;

  @Override
  public UserJpaEntity findById(Long id) {
    return Optional.ofNullable(em.find(UserJpaEntity.class, id))
        .orElseThrow(UserNotFoundException::new);
  }
}
