package com.yapp.artie.domain.user.adapter.out.persistence;

import com.yapp.artie.domain.user.domain.User;
import org.springframework.stereotype.Component;

@Component
class UserMapper {

  User mapToDomainEntity(UserJpaEntity user) {
    return User.withId(user.getId(),
        user.getUid(),
        user.getProfileImage(),
        user.getName()
    );
  }

  UserJpaEntity mapToJpaEntity(User user) {
    return new UserJpaEntity(
        user.getId(),
        user.getUid(),
        user.getProfileImage(),
        user.getName()
        );
  }
}
