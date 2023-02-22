package com.yapp.artie.domain.user.domain;

import com.yapp.artie.global.common.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String uid;

  private String name;

  private String profileImage;

  public static User create(String uid, String name, String picture) {
    User user = new User();
    user.setUid(uid);
    user.setName(name);
    user.setProfileImage(picture);
    return user;
  }

  public static User create(Long id, String uid, String name, String profileImage) {
    User user = new User();
    user.id = id;
    user.uid = uid;
    user.name = name;
    user.profileImage = profileImage;
    return user;
  }
}
