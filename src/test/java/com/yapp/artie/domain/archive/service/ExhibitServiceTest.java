package com.yapp.artie.domain.archive.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.yapp.artie.domain.archive.domain.artwork.Artwork;
import com.yapp.artie.domain.archive.domain.category.Category;
import com.yapp.artie.domain.archive.domain.exhibit.Exhibit;
import com.yapp.artie.domain.archive.domain.exhibit.PinType;
import com.yapp.artie.domain.archive.dto.cateogry.CategoryDto;
import com.yapp.artie.domain.archive.dto.cateogry.CreateCategoryRequestDto;
import com.yapp.artie.domain.archive.dto.exhibit.CalendarExhibitRequestDto;
import com.yapp.artie.domain.archive.dto.exhibit.CalendarExhibitResponseDto;
import com.yapp.artie.domain.archive.dto.exhibit.CreateExhibitRequestDto;
import com.yapp.artie.domain.archive.dto.exhibit.ExhibitByDateResponseDto;
import com.yapp.artie.domain.archive.dto.exhibit.PostDetailInfo;
import com.yapp.artie.domain.archive.dto.exhibit.PostInfoByCategoryDto;
import com.yapp.artie.domain.archive.dto.exhibit.PostInfoDto;
import com.yapp.artie.domain.archive.dto.exhibit.UpdateExhibitRequestDto;
import com.yapp.artie.domain.archive.exception.ExhibitAlreadyPublishedException;
import com.yapp.artie.domain.archive.exception.NotOwnerOfCategoryException;
import com.yapp.artie.domain.archive.repository.ArtworkRepository;
import com.yapp.artie.domain.archive.repository.CategoryRepository;
import com.yapp.artie.domain.archive.repository.ExhibitRepository;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
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
  ExhibitRepository exhibitRepository;

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  ArtworkRepository artworkRepository;

  @Autowired
  ExhibitService exhibitService;

  @Autowired
  CategoryService categoryService;

  @Autowired
  ArtworkService artworkService;

  User createUser(String name, String uid) {
    User user = new User();
    user.setName(name);
    user.setUid(uid);
    em.persist(user);
    categoryService.create(new CreateCategoryRequestDto("test"), user.getId());

    return user;
  }

  @Test
  public void getExhibitCount_전시의_개수를_반환한다() throws Exception {
    User user = createUser("user", "tu1");
    CategoryDto defaultCategory = categoryService.categoriesOf(user.getId()).get(0);
    for (int i = 0; i < 5; i++) {
      CreateExhibitRequestDto exhibitRequestDto = new CreateExhibitRequestDto("test",
          defaultCategory.getId(),
          LocalDate.now());
      Long created = exhibitService.create(exhibitRequestDto, user.getId());
      exhibitService.publish(created,user.getId());
    }
    int exhibitCount = exhibitService.getExhibitCount(user.getId());
    assertThat(exhibitCount).isEqualTo(5);
  }

  @Test
  public void create_전시를_생성한다() throws Exception {
    User user = createUser("user", "tu1");
    CategoryDto defaultCategory = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitRequestDto exhibitRequestDto = new CreateExhibitRequestDto("test",
        defaultCategory.getId(),
        LocalDate.now());

    Long created = exhibitService.create(exhibitRequestDto, user.getId());
    Optional<Exhibit> exhibit = Optional.ofNullable(em.find(Exhibit.class, created));

    assertThat(exhibit).isPresent();
  }

  @Test
  public void create_다른_사용자의_카테고리에_전시를_생성하려고_하는_경우_예외를_발생한다() throws Exception {
    User user = createUser("user", "tu");
    User userAnother = createUser("userAnother", "tu2");
    CategoryDto defaultCategory = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitRequestDto exhibitRequestDto = new CreateExhibitRequestDto("test",
        defaultCategory.getId(),
        LocalDate.now());

    assertThatThrownBy(() -> {
      exhibitService.create(exhibitRequestDto, userAnother.getId());
    }).isInstanceOf(NotOwnerOfCategoryException.class);
  }

  @Test
  public void publish_임시저장된_전시를_영구저장한다() throws Exception {
    User user = createUser("user", "tu");
    CategoryDto defaultCategory = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitRequestDto exhibitRequestDto = new CreateExhibitRequestDto("test",
        defaultCategory.getId(),
        LocalDate.now());

    Long created = exhibitService.create(exhibitRequestDto, user.getId());
    exhibitService.publish(created, user.getId());
    Exhibit exhibit = em.find(Exhibit.class, created);

    assertThat(exhibit.isPublished()).isTrue();
  }

  @Test
  public void publish_이미_발행된_전시를_영구_저장_요청하면_예외를_발생한다() throws Exception {
    User user = createUser("user", "tu");
    CategoryDto defaultCategory = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitRequestDto exhibitRequestDto = new CreateExhibitRequestDto("test",
        defaultCategory.getId(),
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
    CategoryDto defaultCategory = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitRequestDto exhibitRequestDto = new CreateExhibitRequestDto("test",
        defaultCategory.getId(),
        LocalDate.now());
    Long created = exhibitService.create(exhibitRequestDto, user.getId());
    exhibitService.publish(created, user.getId());
    Exhibit exhibit = em.find(Exhibit.class, created);

    String updatedName = "rename";
    exhibitService.update(
        new UpdateExhibitRequestDto(updatedName, LocalDate.now(), defaultCategory.getId()),
        exhibit.getId(),
        user.getId());

    PostInfoDto actual = exhibitService.getExhibitInformation(exhibit.getId(),
        user.getId());
    assertThat(actual.getName()).isEqualTo(updatedName);
  }

  @Test
  public void delete_전시를_삭제한다() throws Exception {
    User user = createUser("user", "tu");
    CategoryDto defaultCategory = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitRequestDto exhibitRequestDto = new CreateExhibitRequestDto("test",
        defaultCategory.getId(),
        LocalDate.now());
    Long created = exhibitService.create(exhibitRequestDto, user.getId());
    exhibitService.publish(created, user.getId());

    exhibitService.delete(created, user.getId());

    assertThat(em.find(Exhibit.class, created)).isNull();
  }

  @Test
  public void getExhibitByMonthly_월_별로_전시를_조회한다() throws Exception {
    User user = createUser("user", "tu");
    Category defaultCategory = categoryRepository.findCategoryEntityGraphById(user.getId());

    for (int i = 1; i <= 5; i++) {
      Exhibit exhibit = Exhibit.create("test", LocalDate.now(), defaultCategory, user, null);
      exhibitRepository.save(exhibit);
      exhibit.publish();
      LocalDateTime mockedCreatedAt = i < 5 ?
          YearMonth.of(2023, 1).atDay(i).atStartOfDay()
          : YearMonth.of(2023, 1).atDay(1).atTime(9, 0);
      exhibitRepository.updateExhibitCreatedAt(mockedCreatedAt, exhibit.getId());
      artworkRepository.save(
          Artwork.create(exhibit, true, null, null, String.format("test-%d", i)));
    }

    List<CalendarExhibitResponseDto> results = exhibitService.getExhibitByMonthly(
        new CalendarExhibitRequestDto(2023, 1),
        user.getId());

    assertThat(results.size()).isEqualTo(4);
    assertThat(results.get(0).getImageURL().endsWith("test-5")).isTrue();
    assertThat(results.get(0).getYear()).isEqualTo(2023);
    assertThat(results.get(0).getMonth()).isEqualTo(1);
    assertThat(results.get(0).getDay()).isEqualTo(1);
    assertThat(results.get(0).getPostNum()).isEqualTo(2);
    assertThat(results.get(0).getPostId()).isEqualTo(5L);
    assertThat(results.get(1).getDay()).isEqualTo(2);
  }

  @Test
  public void updatePostPinType_카테고리_상단_설정_CATEGORY_TO_NONE() {
    User user = createUser("user", "tu");
    CategoryDto defaultCategory = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitRequestDto exhibitRequestDto = new CreateExhibitRequestDto("test",
        defaultCategory.getId(),
        LocalDate.now());
    Long exhibitId1 = exhibitService.create(exhibitRequestDto, user.getId());
    Long exhibitId2 = exhibitService.create(exhibitRequestDto, user.getId());

    exhibitService.updatePostPinType(user.getId(), exhibitId2, true, true);
    exhibitService.updatePostPinType(user.getId(), exhibitId1, true, true);

    Optional<Exhibit> exhibit1 = exhibitRepository.findExhibitEntityGraphById(exhibitId1);
    Optional<Exhibit> exhibit2 = exhibitRepository.findExhibitEntityGraphById(exhibitId2);
    assertThat(exhibit1.isPresent()).isTrue();
    assertThat(exhibit1.get().getPinType()).isEqualTo(PinType.CATEGORY);
    assertThat(exhibit2.isPresent()).isTrue();
    assertThat(exhibit2.get().getPinType()).isEqualTo(PinType.NONE);
  }

  @Test
  public void updatePostPinType_전체기록_상단_설정_BOTH_TO_ALL() {
    User user = createUser("user", "tu");
    CategoryDto defaultCategory = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitRequestDto exhibitRequestDto = new CreateExhibitRequestDto("test",
        defaultCategory.getId(),
        LocalDate.now());
    Long exhibitId1 = exhibitService.create(exhibitRequestDto, user.getId());
    Long exhibitId2 = exhibitService.create(exhibitRequestDto, user.getId());

    exhibitService.updatePostPinType(user.getId(), exhibitId2, false, true);
    exhibitService.updatePostPinType(user.getId(), exhibitId2, true, true);
    exhibitService.updatePostPinType(user.getId(), exhibitId1, false, true);

    Optional<Exhibit> exhibit1 = exhibitRepository.findExhibitEntityGraphById(exhibitId1);
    Optional<Exhibit> exhibit2 = exhibitRepository.findExhibitEntityGraphById(exhibitId2);
    assertThat(exhibit1.isPresent()).isTrue();
    assertThat(exhibit1.get().getPinType()).isEqualTo(PinType.ALL);
    assertThat(exhibit2.isPresent()).isTrue();
    assertThat(exhibit2.get().getPinType()).isEqualTo(PinType.CATEGORY);
  }

  @Test
  public void updatePostPinType_전체기록_상단_설정_CATEGORY_TO_BOTH() {
    User user = createUser("user", "tu");
    CategoryDto defaultCategory = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitRequestDto exhibitRequestDto = new CreateExhibitRequestDto("test",
        defaultCategory.getId(),
        LocalDate.now());
    Long exhibitId1 = exhibitService.create(exhibitRequestDto, user.getId());
    Long exhibitId2 = exhibitService.create(exhibitRequestDto, user.getId());

    exhibitService.updatePostPinType(user.getId(), exhibitId2, false, true);
    exhibitService.updatePostPinType(user.getId(), exhibitId1, false, true);
    exhibitService.updatePostPinType(user.getId(), exhibitId1, true, true);

    Optional<Exhibit> exhibit1 = exhibitRepository.findExhibitEntityGraphById(exhibitId1);
    Optional<Exhibit> exhibit2 = exhibitRepository.findExhibitEntityGraphById(exhibitId2);
    assertThat(exhibit1.isPresent()).isTrue();
    assertThat(exhibit1.get().getPinType()).isEqualTo(PinType.BOTH);
    assertThat(exhibit2.isPresent()).isTrue();
    assertThat(exhibit2.get().getPinType()).isEqualTo(PinType.NONE);
  }

  @Test
  public void updatePostPinType_카테고리_상단_설정_해제() {
    User user = createUser("user", "tu");
    CategoryDto defaultCategory = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitRequestDto exhibitRequestDto = new CreateExhibitRequestDto("test",
        defaultCategory.getId(),
        LocalDate.now());
    Long exhibitId1 = exhibitService.create(exhibitRequestDto, user.getId());

    exhibitService.updatePostPinType(user.getId(), exhibitId1, true, true);
    exhibitService.updatePostPinType(user.getId(), exhibitId1, true, false);

    Optional<Exhibit> exhibit1 = exhibitRepository.findExhibitEntityGraphById(exhibitId1);
    assertThat(exhibit1.isPresent()).isTrue();
    assertThat(exhibit1.get().getPinType()).isEqualTo(PinType.NONE);
  }

  @Test
  public void updatePostPinType_전체기록_상단_설정_해제() {
    User user = createUser("user", "tu");
    CategoryDto defaultCategory = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitRequestDto exhibitRequestDto = new CreateExhibitRequestDto("test",
        defaultCategory.getId(),
        LocalDate.now());
    Long exhibitId1 = exhibitService.create(exhibitRequestDto, user.getId());

    exhibitService.updatePostPinType(user.getId(), exhibitId1, true, true);
    exhibitService.updatePostPinType(user.getId(), exhibitId1, false, true);
    exhibitService.updatePostPinType(user.getId(), exhibitId1, true, false);

    Optional<Exhibit> exhibit1 = exhibitRepository.findExhibitEntityGraphById(exhibitId1);
    assertThat(exhibit1.isPresent()).isTrue();
    assertThat(exhibit1.get().getPinType()).isEqualTo(PinType.ALL);
  }

  @Test
  public void getExhibitByPage_상단고정포함_카테고리_전시목록() {
    User user = createUser("user", "tu");
    Category defaultCategory = categoryService.findCategoryWithUser(
        categoryService.categoriesOf(user.getId()).get(0).getId(), user.getId());
    Exhibit exhibit = exhibitRepository.save(
        Exhibit.create("test-0", LocalDate.now(), defaultCategory,
            user, null));
    exhibit.publish();
    for (int i = 1; i <= 10; i++) {
      exhibitRepository.save(
          Exhibit.create(String.format("test-%d", i), LocalDate.now(), defaultCategory,
              user, null)).publish();
    }
    exhibitService.updatePostPinType(user.getId(), exhibit.getId(), false, true);
    exhibitService.updatePostPinType(user.getId(), exhibit.getId(), true, true);

    Page<PostDetailInfo> results = exhibitService.getExhibitByPage(defaultCategory.getId(),
        user.getId(), 0, 5, Direction.DESC);

    assertThat(exhibit.getPinType()).isEqualTo(PinType.BOTH);
    assertThat(results.getSize()).isEqualTo(5);
    assertThat(results.getTotalPages()).isEqualTo(3);
    assertThat(results.getTotalElements()).isEqualTo(11);
    assertThat(results.getNumber()).isEqualTo(0);
    assertThat(results.getContent().get(0).getId()).isEqualTo(exhibit.getId());
    assertThat(results.getContent().get(0).getName()).isEqualTo(exhibit.contents().getName());
  }

  @Test
  public void getExhibitByPage_상단고정포함_전체_전시목록() {
    User user = createUser("user", "tu");
    Category defaultCategory = categoryService.findCategoryWithUser(
        categoryService.categoriesOf(user.getId()).get(0).getId(), user.getId());
    Exhibit exhibit = exhibitRepository.save(
        Exhibit.create("test-0", LocalDate.now(), defaultCategory,
            user, null));
    exhibit.publish();
    for (int i = 1; i <= 10; i++) {
      exhibitRepository.save(
          Exhibit.create(String.format("test-%d", i), LocalDate.now(), defaultCategory,
              user, null)).publish();
    }
    exhibitService.updatePostPinType(user.getId(), exhibit.getId(), false, true);
    exhibitService.updatePostPinType(user.getId(), exhibit.getId(), true, true);

    Page<PostDetailInfo> results = exhibitService.getExhibitByPage(null, user.getId(),
        0, 5, Direction.DESC);

    assertThat(exhibit.getPinType()).isEqualTo(PinType.BOTH);
    assertThat(results.getSize()).isEqualTo(5);
    assertThat(results.getTotalPages()).isEqualTo(3);
    assertThat(results.getTotalElements()).isEqualTo(11);
    assertThat(results.getNumber()).isEqualTo(0);
    assertThat(results.getContent().get(0).getId()).isEqualTo(exhibit.getId());
    assertThat(results.getContent().get(0).getName()).isEqualTo(exhibit.contents().getName());
  }

  @Test
  public void getExhibitThumbnailByCategory_카테고리별_전시목록은_상단고정이_반영되지_않아야힙니다() {
    User user = createUser("user", "tu");
    Category defaultCategory = categoryService.findCategoryWithUser(
        categoryService.categoriesOf(user.getId()).get(0).getId(), user.getId());
    List<String> imageUriList = new ArrayList<>();
    imageUriList.add("sample-uri");

    Exhibit exhibit = exhibitRepository.save(
        Exhibit.create(String.format("test-0"), LocalDate.now(), defaultCategory,
            user, null));
    exhibit.publish();
    artworkService.createBatch(imageUriList, exhibit.getId(), user.getId());
    for (int i = 1; i <= 10; i++) {
      Exhibit exhibitTest = exhibitRepository.save(
          Exhibit.create(String.format("test-%d", i), LocalDate.now(), defaultCategory,
              user, null));
      exhibitTest.publish();
      artworkService.createBatch(imageUriList, exhibitTest.getId(), user.getId());
    }

    exhibitService.updatePostPinType(user.getId(), exhibit.getId(), false, true);
    exhibitService.updatePostPinType(user.getId(), exhibit.getId(), true, true);

    Page<PostInfoByCategoryDto> results = exhibitService.getExhibitThumbnailByCategory(user.getId(),
        defaultCategory.getId(), 0, 5);

    assertThat(exhibit.getPinType()).isEqualTo(PinType.BOTH);
    assertThat(results.getSize()).isEqualTo(5);
    assertThat(results.getTotalPages()).isEqualTo(3);
    assertThat(results.getTotalElements()).isEqualTo(11);
    assertThat(results.getNumber()).isEqualTo(0);
    assertThat(results.getContent().get(0).getId()).isNotEqualTo(exhibit.getId());
    assertThat(results.getContent().get(0).getName()).isEqualTo("test-10");
  }


  @Test
  public void getExhibitsByDate_특정_일자의_전시_목록_조회() throws Exception {
    User user = createUser("user", "tu");
    Category defaultCategory = categoryRepository.findCategoryEntityGraphById(user.getId());

    for (int i = 1; i <= 2; i++) {
      exhibitRepository.save(
              Exhibit.create(String.format("test-%d", i), LocalDate.now(), defaultCategory, user,
                  null))
          .publish();
    }

    List<ExhibitByDateResponseDto> results = exhibitService.getExhibitsByDate(
        user.getId(), LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now()
            .getDayOfMonth());

    assertThat(results.size()).isEqualTo(2);
    assertThat(results.get(0).getPostId()).isEqualTo(2L);
    assertThat(results.get(0).getPostName()).isEqualTo("test-2");
  }
}

