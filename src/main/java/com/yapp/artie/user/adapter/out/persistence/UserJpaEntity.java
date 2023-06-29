package com.yapp.artie.user.adapter.out.persistence;

import com.yapp.artie.global.common.persistence.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "user", indexes = @Index(name = "index_user_uid", columnList = "uid"))
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
  private String profileImage;
  private String name;

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
