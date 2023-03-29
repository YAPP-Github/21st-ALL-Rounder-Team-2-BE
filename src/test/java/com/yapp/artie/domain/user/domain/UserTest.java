package com.yapp.artie.domain.user.domain;

import org.junit.jupiter.api.DisplayName;

@DisplayName("User 테스트")
public class UserTest {

  public static final UserJpaEntity TEST_USER = UserJpaEntity.create("test-uid", "test-name", "test-profile");
  public static final UserJpaEntity TEST_SAVED_USER = UserJpaEntity.create(1L, "test-uid", "test-name",
      "test-profile");
}