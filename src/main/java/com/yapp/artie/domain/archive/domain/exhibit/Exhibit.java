package com.yapp.artie.domain.archive.domain.exhibit;


import com.yapp.artie.domain.archive.domain.artwork.Artwork;
import com.yapp.artie.domain.archive.domain.category.Category;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.global.common.BaseEntity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
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
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @OneToMany(mappedBy = "exhibit", cascade = CascadeType.ALL, orphanRemoval = true)
  List<Artwork> artworks = new ArrayList<>();

  @Embedded
  private ExhibitContents contents;

  @Embedded
  private Publication publication;

  private Exhibit(User user, Category category, ExhibitContents contents, Publication publication) {
    this.user = user;
    this.category = category;
    this.contents = contents;
    this.publication = publication;
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

  public boolean ownedBy(User user) {
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

  public static Exhibit create(String name, LocalDate postDate, Category category, User user) {
    ExhibitContents contents = new ExhibitContents(name, null, null, postDate);
    Publication publication = new Publication();
    return new Exhibit(user, category, contents, publication);
  }

  public void update(String name, LocalDate postDate) {
    this.contents = new ExhibitContents(name, contents().getReview(),
        contents().getAttachedLink(), postDate);
  }
}
