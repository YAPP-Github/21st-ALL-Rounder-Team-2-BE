package com.yapp.artie.domain.archive.domain.tag;

import com.yapp.artie.domain.archive.domain.artwork.Artwork;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.global.common.BaseEntity;
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
public class Tag extends BaseEntity {

  @EmbeddedId
  private TagId id = new TagId();

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("artworkId")
  @JoinColumn(name = "image_id")
  private Artwork artwork;

  @Column(nullable = false, name = "seq")
  private int sequence;

  @Column(nullable = false)
  private String name;

  public Tag(User user, Artwork artwork, int sequence, String name) {
    this.id = new TagId(user.getId(), artwork.getId());
    this.user = user;
    this.artwork = artwork;
    this.sequence = sequence;
    this.name = name;
  }
}
