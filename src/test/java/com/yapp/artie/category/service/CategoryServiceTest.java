package com.yapp.artie.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import com.yapp.artie.category.domain.Category;
import com.yapp.artie.category.domain.CategoryRepository;
import com.yapp.artie.category.dto.CategoryDetailResponse;
import com.yapp.artie.category.dto.CreateCategoryRequest;
import com.yapp.artie.category.dto.UpdateCategoryRequest;
import com.yapp.artie.category.exception.CategoryAlreadyExistException;
import com.yapp.artie.category.exception.CategoryNotFoundException;
import com.yapp.artie.category.exception.ChangeCategoryWrongLengthException;
import com.yapp.artie.category.exception.ExceededCategoryCountException;
import com.yapp.artie.gallery.domain.entity.artwork.Artwork;
import com.yapp.artie.gallery.domain.entity.artwork.Tag;
import com.yapp.artie.gallery.domain.entity.exhibition.Exhibition;
import com.yapp.artie.gallery.domain.repository.ArtworkRepository;
import com.yapp.artie.gallery.domain.repository.ExhibitionRepository;
import com.yapp.artie.gallery.domain.repository.TagRepository;
import com.yapp.artie.user.adapter.out.persistence.UserJpaEntity;
import com.yapp.artie.user.adapter.out.persistence.UserRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class CategoryServiceTest {

  @Autowired
  EntityManager em;

  @Autowired
  EntityManagerFactory emf;

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

  @Autowired
  CategoryService categoryService;

  @BeforeEach
  void setUp() {
    createUser("test", "1");
    createUser("test2", "2");
  }

  private void createUser(String name, String uid) {
    UserJpaEntity user = new UserJpaEntity();
    user.setName(name);
    user.setUid(uid);
    em.persist(user);
  }

  private UserJpaEntity findUser(String uid) {
    return userRepository.findByUid(uid).get();
  }


  private void createCategoryBy(UserJpaEntity user, int count) {
    for (int sequence = 0; sequence < count; sequence++) {
      createCategory(user, Integer.toString(sequence));
    }
  }

  private Long createCategory(UserJpaEntity user, String name) {
    return categoryService.create(new CreateCategoryRequest(name), user.getId());
  }

  private Category findCategory(Long id) {
    return em.find(Category.class, id);
  }

  @Test
  public void findCategoryWithUser_해당_id의_카테고리가_존재하지_않으면_예외를_발생한다() throws Exception {
    assertThatThrownBy(() -> {
      categoryService.findCategoryWithUser(1L, findUser("1").getId());
    }).isInstanceOf(CategoryNotFoundException.class);
  }

  @Test
  public void findCategoryWithUser_카테고리와_함께_유저_프록시도_초기화_해야한다() throws Exception {
    UserJpaEntity user = findUser("1");
    Long created = createCategory(user, "test");
    Category categoryWithUser = categoryService.findCategoryWithUser(created, user.getId());

    assertThat(emf.getPersistenceUnitUtil().isLoaded(categoryWithUser.getUser())).isTrue();
  }

  @Test
  public void categoriesOf_카테고리_목록_정상_조회() {
    UserJpaEntity user = findUser("1");
    Category category1 = categoryRepository.save(Category.create(user, "test-category-1", 1));
    Category category2 = categoryRepository.save(Category.create(user, "test-category-2", 2));
    exhibitionRepository.save(
            Exhibition.create("test", LocalDate.now(), category1, user, null))
        .publish();
    exhibitionRepository.saveAll(
        Arrays.asList(Exhibition.create("test", LocalDate.now(), category1, user, null),
            Exhibition.create("test", LocalDate.now(), category2, user, null)));

    List<CategoryDetailResponse> result = categoryService.categoriesOf(user.getId());

    assertThat(result.size()).isEqualTo(2);
    assertThat(result.get(0).getId()).isEqualTo(category1.getId());
    assertThat(result.get(0).getPostNum()).isEqualTo(1);
    assertThat(result.get(0).getName()).isEqualTo("test-category-1");
    assertThat(result.get(0).getSequence()).isEqualTo(1);
    assertThat(result.get(1).getId()).isEqualTo(category2.getId());
    assertThat(result.get(1).getPostNum()).isEqualTo(0);
    assertThat(result.get(1).getName()).isEqualTo("test-category-2");
    assertThat(result.get(1).getSequence()).isEqualTo(2);
  }

  @Test
  public void categoriesOf_카테고리가_하나도_존재하지_않는다면_빈_리스트를_반환한다() throws Exception {
    List<CategoryDetailResponse> actual = categoryService.categoriesOf(findUser("1").getId());
    assertThat(actual.size()).isEqualTo(0);
  }

  @Test
  public void create_카테고리를_생성한다() throws Exception {
    Long created = createCategory(findUser("1"), "test");
    assertThat(findCategory(created).getId()).isEqualTo(created);
  }

  @Test
  public void create_이미_존재하는_카테고리를_생성하려_시도할_경우_예외를_발생한다() throws Exception {
    assertThatThrownBy(() -> {
      createCategory(findUser("1"), "test");
      createCategory(findUser("1"), "test");
    }).isInstanceOf(CategoryAlreadyExistException.class);
  }

  @Test
  public void create_카테고리의_갯수가_5개_이상일_경우_생성하려_시도할_경우_예외를_발생한다() throws Exception {
    assertThatThrownBy(() -> {
      createCategoryBy(findUser("1"), 6);
    }).isInstanceOf(ExceededCategoryCountException.class);
  }


  @Test
  public void delete_카테고리를_삭제한다() throws Exception {
    UserJpaEntity user = findUser("1");
    Long created = createCategory(user, "test");
    categoryService.delete(created, user.getId());
    assertThat(Optional.ofNullable(findCategory(created))).isNotPresent();
  }

  @Test
  public void delete_카테고리를_삭제하면_전시데이터도_삭제된다() throws Exception {
    UserJpaEntity user = findUser("1");
    Long created = createCategory(user, "test");
    Exhibition exhibition = exhibitionRepository.save(
        Exhibition.create("test", LocalDate.now(), findCategory(created), user, "link"));
    Artwork artwork = artworkRepository.save(Artwork.create(exhibition, true, "url"));
    Tag tag = tagRepository.save(new Tag(user, artwork, 1, "tagName"));
    em.clear();

    categoryService.delete(created, user.getId());
    em.flush();

    assertThat(Optional.ofNullable(findCategory(created))).isNotPresent();
    assertThat(exhibitionRepository.findById(exhibition.getId()).isEmpty()).isTrue();
    assertThat(artworkRepository.findById(artwork.getId()).isEmpty()).isTrue();
    assertThat(tagRepository.findById(tag.getId()).isEmpty()).isTrue();
  }

  @Test
  public void delete_존재하지_않는_카테고리를_삭제하면_예외를_발생한다() throws Exception {
    assertThatThrownBy(() -> {
      categoryService.delete(1L, findUser("1").getId());
    }).isInstanceOf(CategoryNotFoundException.class);
  }

  @Test
  public void delete_다른사람의_카테고리를_삭제하려_시도할_경우_예외를_발생한다() throws Exception {
    UserJpaEntity user1 = findUser("1");
    UserJpaEntity user2 = findUser("2");
    Long created = createCategory(user2, "test");

    assertThatThrownBy(() -> {
      categoryService.delete(created, user1.getId());
    }).isInstanceOf(CategoryNotFoundException.class);
  }

  @Test
  public void update_카테고리를_수정한다() throws Exception {
    UserJpaEntity user = findUser("1");
    Long created = createCategory(user, "test");
    categoryService.update(new UpdateCategoryRequest("rename"), created, user.getId());
    assertThat(findCategory(created).getName()).isEqualTo("rename");
  }

  @Test
  public void update_다른사람의_카테고리를_수정하려_시도할_경우_예외를_발생한다() throws Exception {
    UserJpaEntity user1 = findUser("1");
    UserJpaEntity user2 = findUser("2");
    Long created = createCategory(user2, "test");

    assertThatThrownBy(() -> {
      categoryService.update(new UpdateCategoryRequest("rename"), created, user1.getId());
    }).isInstanceOf(CategoryNotFoundException.class);
  }

  @Test
  public void sequence_카테고리_생성_시_시퀀스가_오름차순으로_생성된다() throws Exception {
    UserJpaEntity user = findUser("1");
    createCategoryBy(user, 5);
    List<CategoryDetailResponse> categories = categoryService.categoriesOf(user.getId());
    for (int expected = 0; expected < categories.size(); expected++) {
      assertThat(categories.get(expected).getSequence())
          .isEqualTo(expected);
    }
  }

  @ParameterizedTest(name = "[시퀀스 벌크성 업데이트 테스트 #{index}] => {0}번 카테고리가 삭제될 경우")
  @ValueSource(ints = {1, 2, 3, 4, 5})
  public void sequence_카테고리_삭제_시_시퀀스가_누락된_숫자없이_오름차순으로_정렬된다(int target) throws Exception {
    UserJpaEntity user = findUser("1");
    createCategoryBy(user, 5);

    List<CategoryDetailResponse> categories = categoryService.categoriesOf(user.getId());
    CategoryDetailResponse deleted = categories.get(target - 1);
    categoryService.delete(deleted.getId(), user.getId());

    List<CategoryDetailResponse> actualCategories = categoryService.categoriesOf(user.getId());
    for (int expected = 0; expected < actualCategories.size(); expected++) {
      assertThat(actualCategories.get(expected).getSequence())
          .isEqualTo(expected);
    }
  }

  @Test
  public void shuffle_카테고리의_순서를_변경한다() throws Exception {
    UserJpaEntity user = findUser("1");
    createCategoryBy(user, 5);
    List<Category> originCategories = categoryRepository.findCategoriesByUserOrderBySequence(
        user);
    List<CategoryDetailResponse> shuffled = List.of(
        new CategoryDetailResponse(originCategories.get(0).getId(), "0", 1),
        new CategoryDetailResponse(originCategories.get(1).getId(), "1", 3),
        new CategoryDetailResponse(originCategories.get(2).getId(), "2", 4),
        new CategoryDetailResponse(originCategories.get(3).getId(), "3", 2),
        new CategoryDetailResponse(originCategories.get(4).getId(), "4", 0)
    );

    categoryService.shuffle(shuffled, user.getId());

    List<Category> shuffledCategories = categoryRepository.findCategoriesByUserOrderBySequence(
        user);
    long[] shuffledIds = new long[5];
    for (int i = 0; i < 5; i++) {
      shuffledIds[i] = shuffledCategories.get(i).getId();
    }

    assertArrayEquals(new long[]{
        originCategories.get(4).getId(),
        originCategories.get(0).getId(),
        originCategories.get(3).getId(),
        originCategories.get(1).getId(),
        originCategories.get(2).getId()
    }, shuffledIds);
  }

  @Test
  public void shuffle_주어진_리스트가_원본_카테고리의_수와_같지않으면_예외를_발생한다() throws Exception {
    UserJpaEntity user = findUser("1");
    createCategoryBy(user, 5);

    assertThatThrownBy(() -> {
      categoryService.shuffle(List.of(new CategoryDetailResponse(1L, "test", 1)), user.getId());
    }).isInstanceOf(ChangeCategoryWrongLengthException.class);
  }
}
