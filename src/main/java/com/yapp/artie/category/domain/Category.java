package com.yapp.artie.category.domain;

import com.yapp.artie.category.domain.exception.InvalidCategoryNameException;
import com.yapp.artie.category.domain.exception.InvalidSequenceException;
import com.yapp.artie.gallery.domain.entity.exhibition.Exhibition;
import com.yapp.artie.global.common.persistence.BaseEntity;
import com.yapp.artie.user.adapter.out.persistence.UserJpaEntity;
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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserJpaEntity user;

  @Column(nullable = false)
  private String name;

  @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
  List<Exhibition> exhibitions = new ArrayList<>();

  @Column(nullable = false, name = "seq")
  private int sequence;

  private Category(UserJpaEntity user, String name, int sequence) {
    validateSequenceRange(sequence);
    validateNameLength(name);

    this.user = user;
    this.name = name;
    this.sequence = sequence;
  }

  public static Category create(UserJpaEntity user, String name, int sequence) {
    return new Category(user, name, sequence);
  }

  public boolean ownedBy(UserJpaEntity user) {
    return this.user.equals(user);
  }

  public void rename(String name) {
    validateNameLength(name);

    this.name = name;
  }

  public void rearrange(int sequence) {
    validateSequenceRange(sequence);

    this.sequence = sequence;
  }

  private void validateSequenceRange(int sequence) {
    if (sequence < 0 || sequence > 4) {
      throw new InvalidSequenceException();
    }
  }

  private void validateNameLength(String name) {
    if (name == null || name.isBlank()) {
      throw new InvalidCategoryNameException();
    }
  }
}
