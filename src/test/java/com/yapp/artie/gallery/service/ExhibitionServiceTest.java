package com.yapp.artie.gallery.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.yapp.artie.category.domain.Category;
import com.yapp.artie.category.domain.CategoryRepository;
import com.yapp.artie.category.dto.CategoryDetailResponse;
import com.yapp.artie.category.dto.CreateCategoryRequest;
import com.yapp.artie.category.exception.NotOwnerOfCategoryException;
import com.yapp.artie.category.service.CategoryService;
import com.yapp.artie.gallery.domain.entity.artwork.Artwork;
import com.yapp.artie.gallery.domain.entity.exhibition.Exhibition;
import com.yapp.artie.gallery.domain.entity.exhibition.PinType;
import com.yapp.artie.gallery.domain.repository.ArtworkRepository;
import com.yapp.artie.gallery.domain.repository.ExhibitionRepository;
import com.yapp.artie.gallery.domain.repository.TagRepository;
import com.yapp.artie.gallery.dto.artwork.CreateArtworkRequest;
import com.yapp.artie.gallery.dto.exhibition.CreateExhibitionRequest;
import com.yapp.artie.gallery.dto.exhibition.ExhibitionByCategoryResponse;
import com.yapp.artie.gallery.dto.exhibition.ExhibitionByDateResponse;
import com.yapp.artie.gallery.dto.exhibition.ExhibitionByMonthlyResponse;
import com.yapp.artie.gallery.dto.exhibition.ExhibitionDetailResponse;
import com.yapp.artie.gallery.dto.exhibition.UpdateExhibitionRequest;
import com.yapp.artie.gallery.exception.ExhibitionAlreadyPublishedException;
import com.yapp.artie.user.adapter.out.persistence.UserJpaEntity;
import com.yapp.artie.user.adapter.out.persistence.UserRepository;
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
class ExhibitionServiceTest {

  @Autowired
  EntityManager em;

  @Autowired
  EntityManagerFactory emf;

  @Autowired
  UserRepository userRepository;

  @Autowired
  ExhibitionRepository exhibitionRepository;

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  ArtworkRepository artworkRepository;

  @Autowired
  ExhibitionService exhibitionService;

  @Autowired
  CategoryService categoryService;

  @Autowired
  ArtworkService artworkService;

  @Autowired
  TagRepository tagRepository;

  UserJpaEntity createUser(String name, String uid) {
    UserJpaEntity user = new UserJpaEntity();
    user.setName(name);
    user.setUid(uid);
    em.persist(user);
    categoryService.create(new CreateCategoryRequest("test"), user.getId());

    return user;
  }

  @Test
  public void getExhibitCount_전시의_개수를_반환한다() throws Exception {
    UserJpaEntity user = createUser("user", "tu1");
    CategoryDetailResponse defaultCategory = categoryService.categoriesOf(user.getId()).get(0);
    for (int i = 0; i < 5; i++) {
      CreateExhibitionRequest exhibitRequestDto = new CreateExhibitionRequest("test",
          defaultCategory.getId(),
          LocalDate.now(), null);
      Long created = exhibitionService.create(exhibitRequestDto, user.getId());
      exhibitionService.publish(created, user.getId());
    }
    int exhibitCount = exhibitionService.getExhibitionCount(user.getId());
    assertThat(exhibitCount).isEqualTo(5);
  }

  @Test
  public void create_전시를_생성한다() throws Exception {
    UserJpaEntity user = createUser("user", "tu1");
    CategoryDetailResponse defaultCategory = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitionRequest exhibitRequestDto = new CreateExhibitionRequest("test",
        defaultCategory.getId(),
        LocalDate.now(), null);

    Long created = exhibitionService.create(exhibitRequestDto, user.getId());
    Optional<Exhibition> exhibit = Optional.ofNullable(em.find(Exhibition.class, created));

    assertThat(exhibit).isPresent();
  }

  @Test
  public void create_다른_사용자의_카테고리에_전시를_생성하려고_하는_경우_예외를_발생한다() throws Exception {
    UserJpaEntity user = createUser("user", "tu");
    UserJpaEntity userAnother = createUser("userAnother", "tu2");
    CategoryDetailResponse defaultCategory = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitionRequest exhibitRequestDto = new CreateExhibitionRequest("test",
        defaultCategory.getId(),
        LocalDate.now(), null);

    assertThatThrownBy(() -> {
      exhibitionService.create(exhibitRequestDto, userAnother.getId());
    }).isInstanceOf(NotOwnerOfCategoryException.class);
  }

  @Test
  public void publish_임시저장된_전시를_영구저장한다() throws Exception {
    UserJpaEntity user = createUser("user", "tu");
    CategoryDetailResponse defaultCategory = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitionRequest exhibitRequestDto = new CreateExhibitionRequest("test",
        defaultCategory.getId(),
        LocalDate.now(), null);

    Long created = exhibitionService.create(exhibitRequestDto, user.getId());
    exhibitionService.publish(created, user.getId());
    Exhibition exhibition = em.find(Exhibition.class, created);

    assertThat(exhibition.isPublished()).isTrue();
  }

  @Test
  public void publish_이미_발행된_전시를_영구_저장_요청하면_예외를_발생한다() throws Exception {
    UserJpaEntity user = createUser("user", "tu");
    CategoryDetailResponse defaultCategory = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitionRequest exhibitRequestDto = new CreateExhibitionRequest("test",
        defaultCategory.getId(),
        LocalDate.now(), null);

    Long created = exhibitionService.create(exhibitRequestDto, user.getId());
    exhibitionService.publish(created, user.getId());
    Exhibition exhibition = em.find(Exhibition.class, created);

    assertThatThrownBy(() -> {
      exhibitionService.publish(exhibition.getId(), user.getId());
    }).isInstanceOf(ExhibitionAlreadyPublishedException.class);
  }

  @Test
  public void update_전시를_수정한다() throws Exception {
    UserJpaEntity user = createUser("user", "tu");
    CategoryDetailResponse defaultCategory = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitionRequest exhibitRequestDto = new CreateExhibitionRequest("test",
        defaultCategory.getId(),
        LocalDate.now(), null);
    Long created = exhibitionService.create(exhibitRequestDto, user.getId());
    exhibitionService.publish(created, user.getId());
    Exhibition exhibition = em.find(Exhibition.class, created);

    String updatedName = "rename";
    String updatedLink = "www.artie.com";
    exhibitionService.update(
        new UpdateExhibitionRequest(updatedName, LocalDate.now(), defaultCategory.getId(),
            updatedLink),
        exhibition.getId(),
        user.getId());

    ExhibitionDetailResponse actual = exhibitionService.getExhibition(exhibition.getId(),
        user.getId());
    assertThat(actual.getName()).isEqualTo(updatedName);
    assertThat(actual.getAttachedLink()).isEqualTo(updatedLink);
  }

  @Test
  public void delete_전시를_삭제한다() throws Exception {
    UserJpaEntity user = createUser("user", "tu");
    CategoryDetailResponse defaultCategory = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitionRequest exhibitRequestDto = new CreateExhibitionRequest("test",
        defaultCategory.getId(),
        LocalDate.now(), null);
    Long created = exhibitionService.create(exhibitRequestDto, user.getId());
    List<String> tags = new ArrayList<>();
    tags.add("test-tag");
    Long artworkId = artworkService.create(
        new CreateArtworkRequest(created, "test-uri", "test-artist", null, tags), user.getId());
    em.clear();

    exhibitionService.delete(created, user.getId());
    em.flush();

    assertThat(exhibitionRepository.findById(created).isEmpty()).isTrue();
    assertThat(artworkRepository.findById(artworkId).isEmpty()).isTrue();
    assertThat(tagRepository.findAllByName(tags.get(0)).size()).isEqualTo(0);
  }

  @Test
  public void getExhibitByMonthly_월_별로_전시를_조회한다() throws Exception {
    UserJpaEntity user = createUser("user", "tu");
    Category defaultCategory = categoryRepository.findCategoryEntityGraphById(user.getId());

    for (int i = 1; i <= 5; i++) {
      Exhibition exhibition = Exhibition.create("test", LocalDate.now(), defaultCategory, user,
          null);
      exhibitionRepository.save(exhibition);
      exhibition.publish();
      LocalDateTime mockedCreatedAt = i < 5 ?
          YearMonth.of(2023, 1).atDay(i).atStartOfDay()
          : YearMonth.of(2023, 1).atDay(1).atTime(9, 0);
      exhibitionRepository.updateExhibitionCreatedAt(mockedCreatedAt, exhibition.getId());
      artworkRepository.save(
          Artwork.create(exhibition, true, null, null, String.format("test-%d", i)));
    }

    List<ExhibitionByMonthlyResponse> results = exhibitionService.getExhibitionsByMonthly(2023, 1,
        user.getId());

    assertThat(results.size()).isEqualTo(4);
    assertThat(results.get(0).getImageURL().endsWith("test-5")).isTrue();
    assertThat(results.get(0).getPostDate().getYear()).isEqualTo(2023);
    assertThat(results.get(0).getPostDate().getMonthValue()).isEqualTo(1);
    assertThat(results.get(0).getPostDate().getDayOfMonth()).isEqualTo(1);
    assertThat(results.get(0).getPostNum()).isEqualTo(2);
    assertThat(results.get(0).getPostId()).isEqualTo(5L);
    assertThat(results.get(0).getPostDate().toString()).isEqualTo("2023-01-01");
    assertThat(results.get(1).getPostDate().getDayOfMonth()).isEqualTo(2);
  }

  @Test
  public void updatePostPinType_카테고리_상단_설정_CATEGORY_TO_NONE() {
    UserJpaEntity user = createUser("user", "tu");
    CategoryDetailResponse defaultCategory = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitionRequest exhibitRequestDto = new CreateExhibitionRequest("test",
        defaultCategory.getId(),
        LocalDate.now(), null);
    Long exhibitId1 = exhibitionService.create(exhibitRequestDto, user.getId());
    Long exhibitId2 = exhibitionService.create(exhibitRequestDto, user.getId());

    exhibitionService.updateExhibitionPinType(user.getId(), exhibitId2, true, true);
    exhibitionService.updateExhibitionPinType(user.getId(), exhibitId1, true, true);

    Optional<Exhibition> exhibit1 = exhibitionRepository.findExhibitionEntityGraphById(exhibitId1);
    Optional<Exhibition> exhibit2 = exhibitionRepository.findExhibitionEntityGraphById(exhibitId2);
    assertThat(exhibit1.isPresent()).isTrue();
    assertThat(exhibit1.get().getPinType()).isEqualTo(PinType.CATEGORY);
    assertThat(exhibit2.isPresent()).isTrue();
    assertThat(exhibit2.get().getPinType()).isEqualTo(PinType.NONE);
  }

  @Test
  public void updatePostPinType_전체기록_상단_설정_BOTH_TO_ALL() {
    UserJpaEntity user = createUser("user", "tu");
    CategoryDetailResponse defaultCategory = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitionRequest exhibitRequestDto = new CreateExhibitionRequest("test",
        defaultCategory.getId(),
        LocalDate.now(), null);
    Long exhibitId1 = exhibitionService.create(exhibitRequestDto, user.getId());
    Long exhibitId2 = exhibitionService.create(exhibitRequestDto, user.getId());

    exhibitionService.updateExhibitionPinType(user.getId(), exhibitId2, false, true);
    exhibitionService.updateExhibitionPinType(user.getId(), exhibitId2, true, true);
    exhibitionService.updateExhibitionPinType(user.getId(), exhibitId1, false, true);

    Optional<Exhibition> exhibit1 = exhibitionRepository.findExhibitionEntityGraphById(exhibitId1);
    Optional<Exhibition> exhibit2 = exhibitionRepository.findExhibitionEntityGraphById(exhibitId2);
    assertThat(exhibit1.isPresent()).isTrue();
    assertThat(exhibit1.get().getPinType()).isEqualTo(PinType.ALL);
    assertThat(exhibit2.isPresent()).isTrue();
    assertThat(exhibit2.get().getPinType()).isEqualTo(PinType.CATEGORY);
  }

  @Test
  public void updatePostPinType_전체기록_상단_설정_CATEGORY_TO_BOTH() {
    UserJpaEntity user = createUser("user", "tu");
    CategoryDetailResponse defaultCategory = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitionRequest exhibitRequestDto = new CreateExhibitionRequest("test",
        defaultCategory.getId(),
        LocalDate.now(), null);
    Long exhibitId1 = exhibitionService.create(exhibitRequestDto, user.getId());
    Long exhibitId2 = exhibitionService.create(exhibitRequestDto, user.getId());

    exhibitionService.updateExhibitionPinType(user.getId(), exhibitId2, false, true);
    exhibitionService.updateExhibitionPinType(user.getId(), exhibitId1, false, true);
    exhibitionService.updateExhibitionPinType(user.getId(), exhibitId1, true, true);

    Optional<Exhibition> exhibit1 = exhibitionRepository.findExhibitionEntityGraphById(exhibitId1);
    Optional<Exhibition> exhibit2 = exhibitionRepository.findExhibitionEntityGraphById(exhibitId2);
    assertThat(exhibit1.isPresent()).isTrue();
    assertThat(exhibit1.get().getPinType()).isEqualTo(PinType.BOTH);
    assertThat(exhibit2.isPresent()).isTrue();
    assertThat(exhibit2.get().getPinType()).isEqualTo(PinType.NONE);
  }

  @Test
  public void updatePostPinType_카테고리_상단_설정_해제() {
    UserJpaEntity user = createUser("user", "tu");
    CategoryDetailResponse defaultCategory = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitionRequest exhibitRequestDto = new CreateExhibitionRequest("test",
        defaultCategory.getId(),
        LocalDate.now(), null);
    Long exhibitId1 = exhibitionService.create(exhibitRequestDto, user.getId());

    exhibitionService.updateExhibitionPinType(user.getId(), exhibitId1, true, true);
    exhibitionService.updateExhibitionPinType(user.getId(), exhibitId1, true, false);

    Optional<Exhibition> exhibit1 = exhibitionRepository.findExhibitionEntityGraphById(exhibitId1);
    assertThat(exhibit1.isPresent()).isTrue();
    assertThat(exhibit1.get().getPinType()).isEqualTo(PinType.NONE);
  }

  @Test
  public void updatePostPinType_전체기록_상단_설정_해제() {
    UserJpaEntity user = createUser("user", "tu");
    CategoryDetailResponse defaultCategory = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitionRequest exhibitRequestDto = new CreateExhibitionRequest("test",
        defaultCategory.getId(),
        LocalDate.now(), null);
    Long exhibitId1 = exhibitionService.create(exhibitRequestDto, user.getId());

    exhibitionService.updateExhibitionPinType(user.getId(), exhibitId1, true, true);
    exhibitionService.updateExhibitionPinType(user.getId(), exhibitId1, false, true);
    exhibitionService.updateExhibitionPinType(user.getId(), exhibitId1, true, false);

    Optional<Exhibition> exhibit1 = exhibitionRepository.findExhibitionEntityGraphById(exhibitId1);
    assertThat(exhibit1.isPresent()).isTrue();
    assertThat(exhibit1.get().getPinType()).isEqualTo(PinType.ALL);
  }

  @Test
  public void getExhibitByPage_상단고정포함_카테고리_전시목록() {
    UserJpaEntity user = createUser("user", "tu");
    Category defaultCategory = categoryService.findCategoryWithUser(
        categoryService.categoriesOf(user.getId()).get(0).getId(), user.getId());
    Exhibition exhibition = exhibitionRepository.save(
        Exhibition.create("test-0", LocalDate.now(), defaultCategory,
            user, null));
    exhibition.publish();
    for (int i = 1; i <= 10; i++) {
      exhibitionRepository.save(
          Exhibition.create(String.format("test-%d", i), LocalDate.now(), defaultCategory,
              user, null)).publish();
    }
    exhibitionService.updateExhibitionPinType(user.getId(), exhibition.getId(), false, true);
    exhibitionService.updateExhibitionPinType(user.getId(), exhibition.getId(), true, true);

    Page<ExhibitionDetailResponse> results = exhibitionService.getExhibitionsForHomePage(
        defaultCategory.getId(),
        user.getId(), 0, 5, Direction.DESC);

    assertThat(exhibition.getPinType()).isEqualTo(PinType.BOTH);
    assertThat(results.getSize()).isEqualTo(5);
    assertThat(results.getTotalPages()).isEqualTo(3);
    assertThat(results.getTotalElements()).isEqualTo(11);
    assertThat(results.getNumber()).isEqualTo(0);
    assertThat(results.getContent().get(0).getId()).isEqualTo(exhibition.getId());
    assertThat(results.getContent().get(0).getName()).isEqualTo(exhibition.contents().getName());
    assertThat(results.getContent().get(0).isPinned()).isEqualTo(true);
    assertThat(results.getContent().get(1).isPinned()).isEqualTo(false);
  }

  @Test
  public void getExhibitByPage_상단고정포함_전체_전시목록() {
    UserJpaEntity user = createUser("user", "tu");
    Category defaultCategory = categoryService.findCategoryWithUser(
        categoryService.categoriesOf(user.getId()).get(0).getId(), user.getId());
    Exhibition exhibition = exhibitionRepository.save(
        Exhibition.create("test-0", LocalDate.now(), defaultCategory,
            user, null));
    exhibition.publish();
    for (int i = 1; i <= 10; i++) {
      exhibitionRepository.save(
          Exhibition.create(String.format("test-%d", i), LocalDate.now(), defaultCategory,
              user, null)).publish();
    }
    exhibitionService.updateExhibitionPinType(user.getId(), exhibition.getId(), false, true);
    exhibitionService.updateExhibitionPinType(user.getId(), exhibition.getId(), true, true);

    Page<ExhibitionDetailResponse> results = exhibitionService.getExhibitionsForHomePage(null,
        user.getId(),
        0, 5, Direction.DESC);

    assertThat(exhibition.getPinType()).isEqualTo(PinType.BOTH);
    assertThat(results.getSize()).isEqualTo(5);
    assertThat(results.getTotalPages()).isEqualTo(3);
    assertThat(results.getTotalElements()).isEqualTo(11);
    assertThat(results.getNumber()).isEqualTo(0);
    assertThat(results.getContent().get(0).getId()).isEqualTo(exhibition.getId());
    assertThat(results.getContent().get(0).getName()).isEqualTo(exhibition.contents().getName());
    assertThat(results.getContent().get(0).isPinned()).isEqualTo(true);
    assertThat(results.getContent().get(1).isPinned()).isEqualTo(false);
  }

  @Test
  public void getExhibitThumbnailByCategory_카테고리별_전시목록은_상단고정이_반영되지_않아야힙니다() {
    UserJpaEntity user = createUser("user", "tu");
    Category defaultCategory = categoryService.findCategoryWithUser(
        categoryService.categoriesOf(user.getId()).get(0).getId(), user.getId());
    List<String> imageUriList = new ArrayList<>();
    imageUriList.add("sample-uri");

    Exhibition exhibition = exhibitionRepository.save(
        Exhibition.create(String.format("test-0"), LocalDate.now(), defaultCategory,
            user, null));
    exhibition.publish();
    artworkService.createBatch(imageUriList, exhibition.getId(), user.getId());
    for (int i = 1; i <= 10; i++) {
      Exhibition exhibitionTest = exhibitionRepository.save(
          Exhibition.create(String.format("test-%d", i), LocalDate.now(), defaultCategory,
              user, null));
      exhibitionTest.publish();
      artworkService.createBatch(imageUriList, exhibitionTest.getId(), user.getId());
    }

    exhibitionService.updateExhibitionPinType(user.getId(), exhibition.getId(), false, true);
    exhibitionService.updateExhibitionPinType(user.getId(), exhibition.getId(), true, true);

    Page<ExhibitionByCategoryResponse> results = exhibitionService.getExhibitionsByCategory(
        user.getId(),
        defaultCategory.getId(), 0, 5);

    assertThat(exhibition.getPinType()).isEqualTo(PinType.BOTH);
    assertThat(results.getSize()).isEqualTo(5);
    assertThat(results.getTotalPages()).isEqualTo(3);
    assertThat(results.getTotalElements()).isEqualTo(11);
    assertThat(results.getNumber()).isEqualTo(0);
    assertThat(results.getContent().get(0).getId()).isNotEqualTo(exhibition.getId());
    assertThat(results.getContent().get(0).getName()).isEqualTo("test-10");
  }


  @Test
  public void getExhibitsByDate_특정_일자의_전시_목록_조회() throws Exception {
    UserJpaEntity user = createUser("user", "tu");
    Category defaultCategory = categoryRepository.findCategoryEntityGraphById(user.getId());

    for (int i = 1; i <= 2; i++) {
      exhibitionRepository.save(
              Exhibition.create(String.format("test-%d", i), LocalDate.now(), defaultCategory, user,
                  null))
          .publish();
    }

    List<ExhibitionByDateResponse> results = exhibitionService.getExhibitionsByDate(
        user.getId(), LocalDate.now().getYear(), LocalDate.now().getMonthValue(), LocalDate.now()
            .getDayOfMonth());

    assertThat(results.size()).isEqualTo(2);
    assertThat(results.get(0).getPostId()).isEqualTo(2L);
    assertThat(results.get(0).getPostName()).isEqualTo("test-2");
  }
}

