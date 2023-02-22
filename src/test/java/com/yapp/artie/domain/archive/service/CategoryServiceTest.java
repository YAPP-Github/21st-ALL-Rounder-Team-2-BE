package com.yapp.artie.domain.archive.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.yapp.artie.domain.archive.domain.artwork.Artwork;
import com.yapp.artie.domain.archive.domain.category.Category;
import com.yapp.artie.domain.archive.domain.exhibit.Exhibit;
import com.yapp.artie.domain.archive.domain.tag.Tag;
import com.yapp.artie.domain.archive.dto.cateogry.CategoryDto;
import com.yapp.artie.domain.archive.dto.cateogry.CreateCategoryRequestDto;
import com.yapp.artie.domain.archive.dto.cateogry.UpdateCategoryRequestDto;
import com.yapp.artie.domain.archive.exception.CategoryAlreadyExistException;
import com.yapp.artie.domain.archive.exception.CategoryNotFoundException;
import com.yapp.artie.domain.archive.exception.ChangeCategoryWrongLengthException;
import com.yapp.artie.domain.archive.exception.ExceededCategoryCountException;
import com.yapp.artie.domain.archive.exception.NotOwnerOfCategoryException;
import com.yapp.artie.domain.archive.repository.ArtworkRepository;
import com.yapp.artie.domain.archive.repository.CategoryRepository;
import com.yapp.artie.domain.archive.repository.ExhibitRepository;
import com.yapp.artie.domain.archive.repository.TagRepository;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
  ExhibitRepository exhibitRepository;

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
    User user = new User();
    user.setName(name);
    user.setUid(uid);
    em.persist(user);
  }

  private User findUser(String uid) {
    return userRepository.findByUid(uid).get();
  }


  private void createCategoryBy(User user, int count) {
    for (int sequence = 0; sequence < count; sequence++) {
      createCategory(user, Integer.toString(sequence));
    }
  }

  private Long createCategory(User user, String name) {
    return categoryService.create(new CreateCategoryRequestDto(name), user.getId());
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
    User user = findUser("1");
    Long created = createCategory(user, "test");
    Category categoryWithUser = categoryService.findCategoryWithUser(created, user.getId());

    assertThat(emf.getPersistenceUnitUtil().isLoaded(categoryWithUser.getUser())).isTrue();
  }

  @Test
  public void categoriesOf_카테고리_목록_정상_조회() {
    User user = findUser("1");
    Category category1 = categoryRepository.save(Category.create(user, "test-category-1", 1));
    Category category2 = categoryRepository.save(Category.create(user, "test-category-2", 2));
    exhibitRepository.save(
            Exhibit.create("test", LocalDate.now(), category1, user, null))
        .publish();
    exhibitRepository.saveAll(
        Arrays.asList(Exhibit.create("test", LocalDate.now(), category1, user, null),
            Exhibit.create("test", LocalDate.now(), category2, user, null)));

    List<CategoryDto> result = categoryService.categoriesOf(user.getId());

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
  public void categoriesOf_카테고리가_하나도_존재하지_않으면_예외를_발생한다() throws Exception {
    assertThatThrownBy(() -> {
      categoryService.categoriesOf(findUser("1").getId());
    }).isInstanceOf(CategoryNotFoundException.class);
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
    User user = findUser("1");
    Long created = createCategory(user, "test");
    categoryService.delete(created, user.getId());
    assertThat(Optional.ofNullable(findCategory(created))).isNotPresent();
  }

  @Test
  public void delete_카테고리를_삭제하면_전시데이터도_삭제된다() throws Exception {
    User user = findUser("1");
    Long created = createCategory(user, "test");
    Exhibit exhibit = exhibitRepository.save(
        Exhibit.create("test", LocalDate.now(), findCategory(created), user, "link"));
    Artwork artwork = artworkRepository.save(Artwork.create(exhibit, true, "url"));
    Tag tag = tagRepository.save(new Tag(user, artwork, 1, "tagName"));
    em.clear();

    categoryService.delete(created, user.getId());
    em.flush();

    assertThat(Optional.ofNullable(findCategory(created))).isNotPresent();
    assertThat(exhibitRepository.findById(exhibit.getId()).isEmpty()).isTrue();
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
    User user1 = findUser("1");
    User user2 = findUser("2");
    Long created = createCategory(user2, "test");

    assertThatThrownBy(() -> {
      categoryService.delete(created, user1.getId());
    }).isInstanceOf(NotOwnerOfCategoryException.class);
  }

  @Test
  public void update_카테고리를_수정한다() throws Exception {
    User user = findUser("1");
    Long created = createCategory(user, "test");
    categoryService.update(new UpdateCategoryRequestDto("rename"), created, user.getId());
    assertThat(findCategory(created).getName()).isEqualTo("rename");
  }

  @Test
  public void update_다른사람의_카테고리를_수정하려_시도할_경우_예외를_발생한다() throws Exception {
    User user1 = findUser("1");
    User user2 = findUser("2");
    Long created = createCategory(user2, "test");

    assertThatThrownBy(() -> {
      categoryService.update(new UpdateCategoryRequestDto("rename"), created, user1.getId());
    }).isInstanceOf(NotOwnerOfCategoryException.class);
  }

  @Test
  public void sequence_카테고리_생성_시_시퀀스가_오름차순으로_생성된다() throws Exception {
    User user = findUser("1");
    createCategoryBy(user, 5);
    List<CategoryDto> categories = categoryService.categoriesOf(user.getId());
    for (int expected = 0; expected < categories.size(); expected++) {
      assertThat(categories.get(expected).getSequence())
          .isEqualTo(expected);
    }
  }

  @ParameterizedTest(name = "[시퀀스 벌크성 업데이트 테스트 #{index}] => {0}번 카테고리가 삭제될 경우")
  @ValueSource(ints = {1, 2, 3, 4, 5})
  public void sequence_카테고리_삭제_시_시퀀스가_누락된_숫자없이_오름차순으로_정렬된다(int target) throws Exception {
    User user = findUser("1");
    createCategoryBy(user, 5);

    List<CategoryDto> categories = categoryService.categoriesOf(user.getId());
    CategoryDto deleted = categories.get(target - 1);
    categoryService.delete(deleted.getId(), user.getId());

    List<CategoryDto> actualCategories = categoryService.categoriesOf(user.getId());
    for (int expected = 0; expected < actualCategories.size(); expected++) {
      assertThat(actualCategories.get(expected).getSequence())
          .isEqualTo(expected);
    }
  }

  @ParameterizedTest(name = "[카테고리 순서 변경 테스트 #{index}] => {0}순으로 재배열하는 경우")
  @ValueSource(strings = {"01234", "12430", "14023", "43210", "02134", "20431", "12304", "12034",
      "43021", "32140", "32014", "1230", "0321", "021", "201", "102", "01", "10", "0"})
  public void shuffle_카테고리의_순서를_변경한다(String expected) throws Exception {
    //given
    List<Integer> expectedList = Arrays.stream(expected.split(""))
        .mapToInt(Integer::parseInt)
        .boxed()
        .collect(Collectors.toList());
    User user = findUser("1");
    createCategoryBy(user, expected.length());

    //when
    List<CategoryDto> shuffled = new ArrayList<>();
    List<CategoryDto> categories = categoryService.categoriesOf(user.getId());
    expectedList.forEach(index -> {
      shuffled.add(categories.get(index));
    });
    categoryService.shuffle(shuffled, user.getId());

    //then
    StringBuilder actual = new StringBuilder();
    categoryService.categoriesOf(user.getId())
        .forEach(categoryDto -> actual.append(categoryDto.getName()));

    assertThat(actual.toString()).isEqualTo(expected);
  }

  @Test
  public void shuffle_주어진_리스트가_원본_카테고리의_수와_같지않으면_예외를_발생한다() throws Exception {
    User user = findUser("1");
    createCategoryBy(user, 5);

    assertThatThrownBy(() -> {
      categoryService.shuffle(List.of(new CategoryDto(1L, "test", 1)), user.getId());
    }).isInstanceOf(ChangeCategoryWrongLengthException.class);
  }
}