package com.yapp.archiveServer.archive.domain.category;

import com.yapp.archiveServer.user.domain.User;
import lombok.*;

import javax.persistence.*;

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
