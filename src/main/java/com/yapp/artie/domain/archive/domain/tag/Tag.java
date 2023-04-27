package com.yapp.artie.domain.archive.domain.tag;

import com.yapp.artie.domain.archive.domain.artwork.Artwork;
import com.yapp.artie.domain.user.adapter.out.persistence.UserJpaEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserJpaEntity user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "image_id", nullable = false)
  private Artwork artwork;

  @Column(nullable = false, name = "seq")
  private int sequence;

  @Column(nullable = false)
  private String name;

  public Tag(UserJpaEntity user, Artwork artwork, int sequence, String name) {
    this.user = user;
    this.artwork = artwork;
    this.sequence = sequence;
    this.name = name;
  }
}
