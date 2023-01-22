package com.yapp.artie.domain.archive.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.yapp.artie.domain.archive.domain.exhibit.Exhibit;
import com.yapp.artie.domain.archive.dto.cateogry.CategoryDto;
import com.yapp.artie.domain.archive.dto.exhibit.CreateExhibitRequestDto;
import com.yapp.artie.domain.archive.exception.NotOwnerOfCategoryException;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class ExhibitServiceTest {

  @Autowired
  EntityManager em;

  @Autowired
  EntityManagerFactory emf;

  @Autowired
  UserRepository userRepository;

  @Autowired
  ExhibitService exhibitService;

  @Autowired
  CategoryService categoryService;

  User createUser(String name, String uid) {
    User user = new User();
    user.setName(name);
    user.setUid(uid);
    em.persist(user);
    categoryService.createDefault(user.getId());

    return user;
  }

  @Test
  public void create_전시를_생성한다() throws Exception {
    User user = createUser("user", "tu1");
    CategoryDto defaultCateogry = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitRequestDto exhibitRequestDto = new CreateExhibitRequestDto("test", defaultCateogry.getId(),
        LocalDate.now());

    Long created = exhibitService.create(exhibitRequestDto, user.getId());
    Optional<Exhibit> exhibit = Optional.ofNullable(em.find(Exhibit.class, created));

    assertThat(exhibit).isPresent();
  }

  @Test
  public void create_다른_사용자의_카테고리에_전시를_생성하려고_하는_경우_예외를_발생한다() throws Exception {
    User user = createUser("user", "tu");
    User userAnother = createUser("userAnother", "tu2");
    CategoryDto defaultCateogry = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitRequestDto exhibitRequestDto = new CreateExhibitRequestDto("test",
        defaultCateogry.getId(),
        LocalDate.now());

    assertThatThrownBy(() -> {
      exhibitService.create(exhibitRequestDto, userAnother.getId());
    }).isInstanceOf(NotOwnerOfCategoryException.class);
  }
}

