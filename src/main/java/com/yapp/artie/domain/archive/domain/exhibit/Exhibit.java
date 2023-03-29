package com.yapp.artie.domain.archive.domain.exhibit;


import com.yapp.artie.domain.archive.domain.artwork.Artwork;
import com.yapp.artie.domain.archive.domain.category.Category;
import com.yapp.artie.domain.user.adapter.out.persistence.UserJpaEntity;
import com.yapp.artie.global.common.BaseEntity;
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
public class Exhibit extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private UserJpaEntity user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @OneToMany(mappedBy = "exhibit", cascade = CascadeType.REMOVE)
  List<Artwork> artworks = new ArrayList<>();

  @Embedded
  private ExhibitContents contents;

  @Embedded
  private Publication publication;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, columnDefinition = "varchar(32) default 'NONE'")
  private PinType pinType;

  private Exhibit(UserJpaEntity user, Category category, ExhibitContents contents, Publication publication) {
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

  public ExhibitContents contents() {
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

  public static Exhibit create(String name, LocalDate postDate, Category category, UserJpaEntity user,
      String attachedLink) {
    ExhibitContents contents = new ExhibitContents(name, null, attachedLink, postDate);
    Publication publication = new Publication();
    return new Exhibit(user, category, contents, publication);
  }

  public void update(String name, LocalDate postDate, String attachedLink, Category category) {
    this.contents = new ExhibitContents(name, contents().getReview(),
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
