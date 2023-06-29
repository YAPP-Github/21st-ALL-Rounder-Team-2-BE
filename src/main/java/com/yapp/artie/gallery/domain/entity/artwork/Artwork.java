package com.yapp.artie.gallery.domain.entity.artwork;

import com.yapp.artie.gallery.domain.entity.exhibition.Exhibition;
import com.yapp.artie.global.common.persistence.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

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
  private Exhibition exhibition;

  @Column(nullable = false, columnDefinition = "boolean default false", name = "is_main_image")
  private boolean isMain;

  @Embedded
  private ArtworkContents contents;

  @OneToMany(mappedBy = "artwork", cascade = CascadeType.REMOVE)
  List<Tag> tags = new ArrayList<>();

  public Artwork(Exhibition exhibition, boolean isMain, ArtworkContents contents) {
    this.exhibition = exhibition;
    this.isMain = isMain;
    this.contents = contents;
  }

  public void display(Exhibition exhibition) {
    this.exhibition = exhibition;
  }

  public static Artwork create(Exhibition exhibition, boolean isMain, String name,
      String artist, String uri) {
    ArtworkContents contents = new ArtworkContents.Builder(uri).name(name).artist(artist).build();
    return new Artwork(exhibition, isMain, contents);
  }

  public static Artwork create(Exhibition exhibition, boolean isMain, String uri) {
    ArtworkContents contents = new ArtworkContents.Builder(uri).build();
    return new Artwork(exhibition, isMain, contents);
  }

  public void setMainArtwork() {
    this.isMain = true;
  }
}
