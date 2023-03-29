package com.yapp.artie.domain.user.adapter.out.persistence;

import com.yapp.artie.global.common.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserJpaEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String uid;

  private String name;

  private String profileImage;

  public static UserJpaEntity create(String uid, String name, String picture) {
    UserJpaEntity user = new UserJpaEntity();
    user.setUid(uid);
    user.setName(name);
    user.setProfileImage(picture);
    return user;
  }

  public static UserJpaEntity create(Long id, String uid, String name, String profileImage) {
    UserJpaEntity user = new UserJpaEntity();
    user.id = id;
    user.uid = uid;
    user.name = name;
    user.profileImage = profileImage;
    return user;
  }
}
