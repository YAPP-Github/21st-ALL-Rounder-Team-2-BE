package com.yapp.artie.domain.user.adapter.out.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<UserJpaEntity, Long> {

  Optional<UserJpaEntity> findByUid(String uid);
}
