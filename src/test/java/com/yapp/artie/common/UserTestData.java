package com.yapp.artie.common;

import com.yapp.artie.user.domain.User;

public class UserTestData {

  public static UserBuilder defaultUser() {

    return new UserBuilder()
        .withId(1L)
        .withUid("uid")
        .withName("Test User")
        .withProfileImage("profileImage.com/user/1/profileImage.png");
  }

  public static class UserBuilder {

    private Long id;
    private String uid;
    private String name;
    private String profileImage;

    public UserBuilder withId(Long id) {
      this.id = id;
      return this;
    }

    public UserBuilder withUid(String uid) {
      this.uid = uid;
      return this;
    }


    public UserBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public UserBuilder withProfileImage(String profileImage) {
      this.profileImage = profileImage;
      return this;
    }

    public User build() {
      return User.withId(
          this.id,
          this.uid,
          this.profileImage,
          this.name
      );
    }
  }
}
