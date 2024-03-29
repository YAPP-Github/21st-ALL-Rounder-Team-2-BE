package com.yapp.artie.category.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yapp.artie.gallery.domain.entity.artwork.Artwork;
import com.yapp.artie.gallery.domain.entity.artwork.Tag;
import com.yapp.artie.gallery.domain.entity.exhibition.Exhibition;
import com.yapp.artie.gallery.domain.repository.ArtworkRepository;
import com.yapp.artie.gallery.domain.repository.ExhibitionRepository;
import com.yapp.artie.gallery.domain.repository.TagRepository;
import com.yapp.artie.user.adapter.out.persistence.UserJpaEntity;
import com.yapp.artie.user.adapter.out.persistence.UserRepository;
import java.time.LocalDate;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class CategoryRepositoryTest {

  public static final UserJpaEntity TEST_SAVED_USER = UserJpaEntity.create(1L, "test-uid",
      "test-name",
      "test-profile");

  @Autowired
  EntityManager em;

  @Autowired
  UserRepository userRepository;

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  ExhibitionRepository exhibitionRepository;

  @Autowired
  ArtworkRepository artworkRepository;

  @Autowired
  TagRepository tagRepository;

  @Test
  @DisplayName("유저 기반 카테고리 삭제 - 카테고리가 삭제되면 해당하는 전시, 작품, 태그도 삭제되어야합니다")
  void deleteAllByUser() {
    UserJpaEntity user = userRepository.save(TEST_SAVED_USER);
    Category category = categoryRepository.save(Category.create(user, "category-name", 1));
    Exhibition exhibition = exhibitionRepository.save(
        Exhibition.create("exhibition-name", LocalDate.now(), category, user, "exhibition-link"));
    Artwork artwork = artworkRepository.save(Artwork.create(exhibition, true, "artwork-uri"));
    Tag tag = tagRepository.save(new Tag(user, artwork, 1, "tag-name"));
    em.flush();
    em.clear();

    categoryRepository.deleteAllByUser(user);
    em.flush();

    assertThat(userRepository.findById(user.getId()).isPresent()).isTrue();
    assertThat(categoryRepository.findById(category.getId()).isPresent()).isFalse();
    assertThat(exhibitionRepository.findById(exhibition.getId()).isPresent()).isFalse();
    assertThat(artworkRepository.findById(artwork.getId()).isPresent()).isFalse();
    assertThat(tagRepository.findById(tag.getId()).isPresent()).isFalse();
  }

  @DisplayName("유저 기반 카테고리 조회")
  @Test
  void findCategoryEntityGraphById() {
    UserJpaEntity user = userRepository.save(TEST_SAVED_USER);
    Category category = categoryRepository.save(Category.create(user, "category-name", 1));
    em.flush();
    em.clear();

    categoryRepository.findUserCategory(category.getId(), user.getId());
  }
}
