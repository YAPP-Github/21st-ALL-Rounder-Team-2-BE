package com.yapp.artie.domain.archive.domain.tag;

import com.yapp.artie.domain.archive.domain.artwork.Artwork;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
