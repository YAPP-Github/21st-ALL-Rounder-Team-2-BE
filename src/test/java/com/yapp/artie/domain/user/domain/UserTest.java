package com.yapp.artie.domain.user.domain;

import org.junit.jupiter.api.DisplayName;

@DisplayName("User 테스트")
public class UserTest {

  public static final User TEST_USER = User.create("test-uid", "test-name", "test-profile");
  public static final User TEST_SAVED_USER = User.create(1L, "test-uid", "test-name",
      "test-profile");
}