package com.yapp.artie.domain.archive.domain.artwork;

import com.yapp.artie.domain.archive.domain.exhibit.Exhibit;
import com.yapp.artie.global.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "image")
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Artwork extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", nullable = false)
  private Exhibit exhibit;

  @Embedded
  private ExposureCondition condition;

  @Embedded
  private ArtworkContents contents;
}
