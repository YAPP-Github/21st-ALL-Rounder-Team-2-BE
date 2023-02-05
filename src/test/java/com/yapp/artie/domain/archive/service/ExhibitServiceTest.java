package com.yapp.artie.domain.archive.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.yapp.artie.domain.archive.domain.exhibit.Exhibit;
import com.yapp.artie.domain.archive.dto.cateogry.CategoryDto;
import com.yapp.artie.domain.archive.dto.cateogry.CreateCategoryRequestDto;
import com.yapp.artie.domain.archive.dto.exhibit.CalendarExhibitRequestDto;
import com.yapp.artie.domain.archive.dto.exhibit.CalendarExhibitResponseDto;
import com.yapp.artie.domain.archive.dto.exhibit.CreateExhibitRequestDto;
import com.yapp.artie.domain.archive.dto.exhibit.PostInfoDto;
import com.yapp.artie.domain.archive.dto.exhibit.UpdateExhibitRequestDto;
import com.yapp.artie.domain.archive.exception.ExhibitAlreadyPublishedException;
import com.yapp.artie.domain.archive.exception.NotOwnerOfCategoryException;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
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
    categoryService.create(new CreateCategoryRequestDto("test"), user.getId());

    return user;
  }

  @Test
  public void create_전시를_생성한다() throws Exception {
    User user = createUser("user", "tu1");
    CategoryDto defaultCateogry = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitRequestDto exhibitRequestDto = new CreateExhibitRequestDto("test",
        defaultCateogry.getId(),
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

  @Test
  public void publish_임시저장된_전시를_영구저장한다() throws Exception {
    User user = createUser("user", "tu");
    CategoryDto defaultCateogry = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitRequestDto exhibitRequestDto = new CreateExhibitRequestDto("test",
        defaultCateogry.getId(),
        LocalDate.now());

    Long created = exhibitService.create(exhibitRequestDto, user.getId());
    exhibitService.publish(created, user.getId());
    Exhibit exhibit = em.find(Exhibit.class, created);

    assertThat(exhibit.isPublished()).isTrue();
  }

  @Test
  public void publish_이미_발행된_전시를_영구_저장_요청하면_예외를_발생한다() throws Exception {
    User user = createUser("user", "tu");
    CategoryDto defaultCateogry = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitRequestDto exhibitRequestDto = new CreateExhibitRequestDto("test",
        defaultCateogry.getId(),
        LocalDate.now());

    Long created = exhibitService.create(exhibitRequestDto, user.getId());
    exhibitService.publish(created, user.getId());
    Exhibit exhibit = em.find(Exhibit.class, created);

    assertThatThrownBy(() -> {
      exhibitService.publish(exhibit.getId(), user.getId());
    }).isInstanceOf(ExhibitAlreadyPublishedException.class);
  }

  @Test
  public void update_전시를_수정한다() throws Exception {
    User user = createUser("user", "tu");
    CategoryDto defaultCateogry = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitRequestDto exhibitRequestDto = new CreateExhibitRequestDto("test",
        defaultCateogry.getId(),
        LocalDate.now());
    Long created = exhibitService.create(exhibitRequestDto, user.getId());
    exhibitService.publish(created, user.getId());
    Exhibit exhibit = em.find(Exhibit.class, created);

    String updatedName = "rename";
    exhibitService.update(
        new UpdateExhibitRequestDto(updatedName, LocalDate.now(), defaultCateogry.getId()),
        exhibit.getId(),
        user.getId());

    PostInfoDto actual = exhibitService.getExhibitInformation(exhibit.getId(),
        user.getId());
    assertThat(actual.getName()).isEqualTo(updatedName);
  }

  @Test
  public void delete_전시를_삭제한다() throws Exception {
    User user = createUser("user", "tu");
    CategoryDto defaultCateogry = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitRequestDto exhibitRequestDto = new CreateExhibitRequestDto("test",
        defaultCateogry.getId(),
        LocalDate.now());
    Long created = exhibitService.create(exhibitRequestDto, user.getId());
    exhibitService.publish(created, user.getId());

    exhibitService.delete(created, user.getId());

    assertThat(em.find(Exhibit.class, created)).isNull();
  }

  @Test
  public void getExhibitByMonthly_월_별로_전시를_조회한다() throws Exception {
    User user = createUser("user", "tu");
    CategoryDto defaultCateogry = categoryService.categoriesOf(user.getId()).get(0);

    for (int i = 1; i <= 12; i++) {
      CreateExhibitRequestDto exhibitRequestDto = new CreateExhibitRequestDto("test",
          defaultCateogry.getId(),
          LocalDate.of(2023, Month.of(i), 1));
      exhibitService.create(exhibitRequestDto, user.getId());
    }

    for (int i = 1; i <= 12; i++) {
      CalendarExhibitRequestDto calendarExhibitRequestDto = new CalendarExhibitRequestDto(2023, i);
      List<CalendarExhibitResponseDto> exhibitByMonthly = exhibitService.getExhibitByMonthly(
          calendarExhibitRequestDto,
          user.getId());

      assertThat(exhibitByMonthly.size()).isEqualTo(1);
    }
  }
}

