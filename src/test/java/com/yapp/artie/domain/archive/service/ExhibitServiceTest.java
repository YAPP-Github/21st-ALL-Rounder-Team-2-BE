package com.yapp.artie.domain.archive.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.yapp.artie.domain.archive.domain.exhibit.Exhibit;
import com.yapp.artie.domain.archive.dto.exhibit.CreateExhibitRequestDto;
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

  User createUser() {
    User user = new User();
    user.setName("test");
    user.setUid("tu1");
    em.persist(user);
    categoryService.createDefault(user.getId());

    return user;
  }

  @Test
  public void create_전시를_생성한다() throws Exception {
    User user = createUser();
    CreateExhibitRequestDto exhibitRequestDto = new CreateExhibitRequestDto("test", 1L,
        LocalDate.now());

    exhibitService.create(exhibitRequestDto, user.getId());
    Optional<Exhibit> exhibit = Optional.ofNullable(em.find(Exhibit.class, 1L));

    assertThat(exhibit).isPresent();
  }

}