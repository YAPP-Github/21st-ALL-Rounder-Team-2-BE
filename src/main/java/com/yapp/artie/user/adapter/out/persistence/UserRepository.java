package com.yapp.artie.user.adapter.out.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserJpaEntity, Long> {

  Optional<UserJpaEntity> findByUid(String uid);
}
