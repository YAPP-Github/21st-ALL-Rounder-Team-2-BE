package com.yapp.artie.gallery.domain.entity.exhibition;


import com.yapp.artie.category.domain.Category;
import com.yapp.artie.gallery.domain.entity.artwork.Artwork;
import com.yapp.artie.global.common.persistence.BaseEntity;
import com.yapp.artie.user.adapter.out.persistence.UserJpaEntity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Exhibition extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserJpaEntity user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @OneToMany(mappedBy = "exhibition", cascade = CascadeType.REMOVE)
  List<Artwork> artworks = new ArrayList<>();

  @Embedded
  private ExhibitionContents contents;

  @Embedded
  private Publication publication;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, columnDefinition = "varchar(32) default 'NONE'")
  private PinType pinType;

  private Exhibition(UserJpaEntity user, Category category, ExhibitionContents contents,
      Publication publication) {
    this.user = user;
    this.category = category;
    this.contents = contents;
    this.publication = publication;
    this.pinType = PinType.NONE;
  }

  public Long getId() {
    return id;
  }

  public Category getCategory() {
    return category;
  }

  public void categorize(Category category) {
    this.category = category;
  }

  public boolean ownedBy(UserJpaEntity user) {
    return this.user.equals(user);
  }

  public ExhibitionContents contents() {
    return this.contents;
  }

  public void publish() {
    publication.publish();
  }

  public boolean isPublished() {
    return publication.isPublished();
  }

  public void addArtwork(Artwork artwork) {
    this.artworks.add(artwork);
    artwork.display(this);
  }

  public static Exhibition create(String name, LocalDate postDate, Category category,
      UserJpaEntity user,
      String attachedLink) {
    ExhibitionContents contents = new ExhibitionContents(name, null, attachedLink, postDate);
    Publication publication = new Publication();
    return new Exhibition(user, category, contents, publication);
  }

  public void update(String name, LocalDate postDate, String attachedLink, Category category) {
    this.contents = new ExhibitionContents(name, contents().getReview(),
        attachedLink, postDate);
    categorize(category);
  }

  public PinType getPinType() {
    return this.pinType;
  }

  public void updatePinType(PinType pinType) {
    this.pinType = pinType;
  }
}
