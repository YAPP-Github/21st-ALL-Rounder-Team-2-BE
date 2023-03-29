package com.yapp.artie.domain.user.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class User {

  private final Long id;
  private final String uid;
  private final String profileImage;
  private String name;

  public static User withoutId(String uid, String name, String profileImage) {
    return new User(null, uid, name, profileImage);
  }

  public static User withId(Long id, String uid, String name, String profileImage) {
    return new User(id, uid, name, profileImage);
  }

  public void rename(String name) {
    this.name = name;
  }
}
