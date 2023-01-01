package com.yapp.artie.archive.domain.tag;

import com.yapp.artie.archive.domain.artwork.Artwork;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "image_tag")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtworkTag {

  @EmbeddedId
  private ArtworkTagId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("tagId")
  @JoinColumn(name = "tag_id")
  private Tag tag;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("artworkId")
  @JoinColumn(name = "image_id")
  private Artwork artwork;

  @Column(nullable = false, name = "seq")
  private int sequence;
}
