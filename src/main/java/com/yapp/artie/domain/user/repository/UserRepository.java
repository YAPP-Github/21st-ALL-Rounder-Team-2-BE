package com.yapp.artie.domain.user.repository;

import com.yapp.artie.domain.user.domain.UserJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserJpaEntity, Long> {

  Optional<UserJpaEntity> findByUid(String uid);
}
