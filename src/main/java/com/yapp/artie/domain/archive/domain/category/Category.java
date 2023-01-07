package com.yapp.artie.domain.archive.domain.category;

import com.yapp.artie.domain.archive.domain.exhibit.Exhibit;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.global.common.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Category(User user, String name) {
    this.user = user;
    this.name = name;
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false)
  private String name;

  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
  List<Exhibit> exhibits = new ArrayList<>();

  public static Category create(User user, String name) {
    return new Category(user, name);
  }

  public void addExhibit(Exhibit exhibit) {
    this.exhibits.add(exhibit);
    exhibit.categorize(this);
  }

  public boolean ownedBy(User user) {
    return this.user.equals(user);
  }

  public void update(String name) {
    this.name = name;
  }
}
