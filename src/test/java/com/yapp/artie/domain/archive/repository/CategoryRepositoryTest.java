package com.yapp.artie.domain.archive.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.yapp.artie.domain.archive.domain.artwork.Artwork;
import com.yapp.artie.domain.archive.domain.category.Category;
import com.yapp.artie.domain.archive.domain.exhibit.Exhibit;
import com.yapp.artie.domain.archive.domain.tag.Tag;
import com.yapp.artie.domain.user.adapter.out.persistence.UserJpaEntity;
import com.yapp.artie.domain.user.domain.UserTest;
import com.yapp.artie.domain.user.adapter.out.persistence.UserRepository;
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

  @Autowired
  EntityManager em;

  @Autowired
  UserRepository userRepository;

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  ExhibitRepository exhibitRepository;

  @Autowired
  ArtworkRepository artworkRepository;

  @Autowired
  TagRepository tagRepository;

  @Test
  @DisplayName("유저 기반 카테고리 삭제 - 카테고리가 삭제되면 해당하는 전시, 작품, 태그도 삭제되어야합니다")
  void deleteAllByUser() {
    UserJpaEntity user = userRepository.save(UserTest.TEST_SAVED_USER);
    Category category = categoryRepository.save(Category.create(user, "category-name", 1));
    Exhibit exhibit = exhibitRepository.save(
        Exhibit.create("exhibit-name", LocalDate.now(), category, user, "exhibit-link"));
    Artwork artwork = artworkRepository.save(Artwork.create(exhibit, true, "artwork-uri"));
    Tag tag = tagRepository.save(new Tag(user, artwork, 1, "tag-name"));
    em.flush();
    em.clear();

    categoryRepository.deleteAllByUser(user);
    em.flush();

    assertThat(userRepository.findById(user.getId()).isPresent()).isTrue();
    assertThat(categoryRepository.findById(category.getId()).isPresent()).isFalse();
    assertThat(exhibitRepository.findById(exhibit.getId()).isPresent()).isFalse();
    assertThat(artworkRepository.findById(artwork.getId()).isPresent()).isFalse();
    assertThat(tagRepository.findById(tag.getId()).isPresent()).isFalse();
  }
}
