package com.yapp.artie.domain.archive.domain.category;

import com.yapp.artie.domain.user.domain.User;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HomeCategory {

  @EmbeddedId
  private HomeCategoryId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("categoryId")
  @JoinColumn(name = "category_id")
  private Category category;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false, name = "seq")
  private int sequence;
}
