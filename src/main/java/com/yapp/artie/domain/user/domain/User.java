package com.yapp.artie.domain.user.domain;

import com.yapp.artie.domain.archive.domain.exhibit.Exhibit;
import com.yapp.artie.global.common.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
  List<Exhibit> categories = new ArrayList<>();

  public static User create(String uid, String name, String picture) {
    User user = new User();
    user.setUid(uid);
    user.setName(name);
    user.setProfileImage(picture);
    return user;
  }
}
