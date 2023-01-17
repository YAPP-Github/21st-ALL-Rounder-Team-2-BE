package com.yapp.artie.domain.archive.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.yapp.artie.domain.archive.domain.category.Category;
import com.yapp.artie.domain.archive.dto.cateogry.CategoryDto;
import com.yapp.artie.domain.archive.dto.cateogry.CreateCategoryRequestDto;
import com.yapp.artie.domain.archive.dto.cateogry.UpdateCategoryRequestDto;
import com.yapp.artie.domain.archive.exception.CategoryAlreadyExistException;
import com.yapp.artie.domain.archive.exception.CategoryNotFoundException;
import com.yapp.artie.domain.archive.exception.ChangeCategoryWrongLengthException;
import com.yapp.artie.domain.archive.exception.ChangeDefaultCategoryException;
import com.yapp.artie.domain.archive.exception.ExceededCategoryCountException;
import com.yapp.artie.domain.archive.exception.NotOwnerOfCategoryException;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.repository.UserRepository;
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
  CategoryService categoryService;

  @BeforeEach
  void setUp() {
    User user = new User();
    user.setName("test");
    user.setUid("tu1");
    em.persist(user);

    User user2 = new User();
    user2.setName("test2");
    user2.setUid("tu2");
    em.persist(user2);
  }

  @Test
  public void findCategoryWithUser_해당_id의_카테고리가_존재하지_않으면_예외를_발생한다() throws Exception {
    User user = userRepository.findByUid("tu1").get();
    assertThatThrownBy(() -> {
      categoryService.findCategoryWithUser(1L);
    }).isInstanceOf(CategoryNotFoundException.class);
  }

  @Test
  public void findCategoryWithUser_카테고리와_함께_유저_프록시도_초기화_해야한다() throws Exception {
    User user = userRepository.findByUid("tu1").get();
    CreateCategoryRequestDto createCategoryRequestDto = new CreateCategoryRequestDto("test");
    Long created = categoryService.create(createCategoryRequestDto, user.getId());
    Category categoryWithUser = categoryService.findCategoryWithUser(created);
    assertThat(emf.getPersistenceUnitUtil().isLoaded(categoryWithUser.getUser())).isTrue();
  }

  @Test
  public void categoriesOf_카테고리가_하나도_존재하지_않으면_예외를_발생한다() throws Exception {
    User user = userRepository.findByUid("tu1").get();
    assertThatThrownBy(() -> {
      categoryService.categoriesOf(user.getId());
    }).isInstanceOf(CategoryNotFoundException.class);
  }

  @Test
  public void create_카테고리를_생성한다() throws Exception {
    User user = userRepository.findByUid("tu1").get();
    CreateCategoryRequestDto createCategoryRequestDto = new CreateCategoryRequestDto("test");
    Long created = categoryService.create(createCategoryRequestDto, user.getId());
    Category find = em.find(Category.class, created);
    assertThat(find.getId()).isEqualTo(created);
  }

  @Test
  public void create_이미_존재하는_카테고리를_생성하려_시도할_경우_예외를_발생한다() throws Exception {
    User user = userRepository.findByUid("tu1").get();
    assertThatThrownBy(() -> {
      CreateCategoryRequestDto createCategoryRequestDto = new CreateCategoryRequestDto("test");
      categoryService.create(createCategoryRequestDto, user.getId());
      categoryService.create(createCategoryRequestDto, user.getId());
    }).isInstanceOf(CategoryAlreadyExistException.class);
  }

  @Test
  public void create_카테고리의_갯수가_5개_이상일_경우_생성하려_시도할_경우_예외를_발생한다() throws Exception {
    User user = userRepository.findByUid("tu1").get();
    assertThatThrownBy(() -> {
      for (int sequence = 0; sequence < 6; sequence++) {
        CreateCategoryRequestDto createCategoryRequestDto = new CreateCategoryRequestDto(
            "test" + sequence);
        categoryService.create(createCategoryRequestDto, user.getId());
      }
    }).isInstanceOf(ExceededCategoryCountException.class);
  }

  @Test
  public void createDefault_기본_카테고리를_생성한다() throws Exception {
    User user = userRepository.findByUid("tu1").get();
    Long created = categoryService.createDefault(user.getId());
    Category find = em.find(Category.class, created);
    assertThat(find.getName()).isEqualTo("전체 기록");
  }

  @Test
  public void delete_카테고리를_삭제한다() throws Exception {
    User user = userRepository.findByUid("tu1").get();
    CreateCategoryRequestDto createCategoryRequestDto = new CreateCategoryRequestDto("test");
    Long created = categoryService.create(createCategoryRequestDto, user.getId());
    categoryService.delete(created, user.getId());
    Category find = em.find(Category.class, created);
    assertThat(Optional.ofNullable(find)).isNotPresent();
  }

  @Test
  public void delete_기본_카테고리를_삭제하면_예외를_발생한다() throws Exception {
    User user = userRepository.findByUid("tu1").get();
    Long created = categoryService.createDefault(user.getId());

    assertThatThrownBy(() -> {
      categoryService.delete(created, user.getId());
    }).isInstanceOf(ChangeDefaultCategoryException.class);
  }

  @Test
  public void delete_존재하지_않는_카테고리를_삭제하면_예외를_발생한다() throws Exception {
    User user = userRepository.findByUid("tu1").get();
    assertThatThrownBy(() -> {
      categoryService.delete(1L, user.getId());
    }).isInstanceOf(CategoryNotFoundException.class);
  }

  @Test
  public void delete_다른사람의_카테고리를_삭제하려_시도할_경우_예외를_발생한다() throws Exception {
    User user1 = userRepository.findByUid("tu1").get();
    User user2 = userRepository.findByUid("tu2").get();
    CreateCategoryRequestDto createCategoryRequestDto = new CreateCategoryRequestDto("test");
    Long created = categoryService.create(createCategoryRequestDto, user2.getId());

    assertThatThrownBy(() -> {
      categoryService.delete(created, user1.getId());
    }).isInstanceOf(NotOwnerOfCategoryException.class);
  }

  @Test
  public void update_카테고리를_수정한다() throws Exception {
    User user = userRepository.findByUid("tu1").get();
    CreateCategoryRequestDto createCategoryRequestDto = new CreateCategoryRequestDto("test");
    Long created = categoryService.create(createCategoryRequestDto, user.getId());
    categoryService.update(new UpdateCategoryRequestDto("rename"), created, user.getId());
    assertThat(em.find(Category.class, created).getName()).isEqualTo("rename");
  }

  @Test
  public void update_기본_카테고리를_수정하려_시도할_경우_예외를_발생한다() throws Exception {
    User user = userRepository.findByUid("tu1").get();
    Long created = categoryService.createDefault(user.getId());

    assertThatThrownBy(() -> {
      categoryService.update(new UpdateCategoryRequestDto("rename"), created, user.getId());
    }).isInstanceOf(ChangeDefaultCategoryException.class);
  }

  @Test
  public void update_다른사람의_카테고리를_수정하려_시도할_경우_예외를_발생한다() throws Exception {
    User user1 = userRepository.findByUid("tu1").get();
    User user2 = userRepository.findByUid("tu2").get();
    CreateCategoryRequestDto createCategoryRequestDto = new CreateCategoryRequestDto("test");
    Long created = categoryService.create(createCategoryRequestDto, user2.getId());

    assertThatThrownBy(() -> {
      categoryService.update(new UpdateCategoryRequestDto("rename"), created, user1.getId());
    }).isInstanceOf(NotOwnerOfCategoryException.class);
  }

  @Test
  public void sequence_카테고리_생성_시_시퀀스가_오름차순으로_생성된다() throws Exception {
    User user = userRepository.findByUid("tu1").get();
    categoryService.createDefault(user.getId());
    for (int sequence = 1; sequence < 5; sequence++) {
      categoryService.create(new CreateCategoryRequestDto("test" + sequence), user.getId());
    }

    List<CategoryDto> categories = categoryService.categoriesOf(user.getId());
    for (int expected = 0; expected < categories.size(); expected++) {
      assertThat(categories.get(expected).getSequence())
          .isEqualTo(expected);
    }
  }

  @ParameterizedTest(name = "[시퀀스 벌크성 업데이트 테스트 #{index}] => {0}번 카테고리가 삭제될 경우")
  @ValueSource(ints = {1, 2, 3, 4})
  public void sequence_카테고리_삭제_시_시퀀스가_누락된_숫자없이_오름차순으로_정렬된다(int target) throws Exception {
    User user = userRepository.findByUid("tu1").get();
    categoryService.createDefault(user.getId());
    for (int sequence = 1; sequence < 5; sequence++) {
      categoryService.create(new CreateCategoryRequestDto("test" + sequence), user.getId());
    }

    List<CategoryDto> categories = categoryService.categoriesOf(user.getId());
    CategoryDto deleted = categories.get(target);
    categoryService.delete(deleted.getId(), user.getId());

    List<CategoryDto> actualCategories = categoryService.categoriesOf(user.getId());
    for (int expected = 0; expected < actualCategories.size(); expected++) {
      assertThat(actualCategories.get(expected).getSequence())
          .isEqualTo(expected);
    }
  }

  @ParameterizedTest(name = "[카테고리 순서 변경 테스트 #{index}] => {0}순으로 재배열하는 경우")
  @ValueSource(strings = {"1234", "1243", "1423", "4321", "2134", "2431"})
  public void shuffle_카테고리의_순서를_변경한다(String expected) throws Exception {
    List<Integer> expectedList = Arrays.stream(expected.split(""))
        .mapToInt(Integer::parseInt)
        .boxed()
        .collect(Collectors.toList());

    User user = userRepository.findByUid("tu1").get();
    categoryService.createDefault(user.getId());
    for (int sequence = 1; sequence < 5; sequence++) {
      categoryService.create(new CreateCategoryRequestDto(Integer.toString(sequence)),
          user.getId());
    }
    List<CategoryDto> shuffled = new ArrayList<>();
    List<CategoryDto> categories = categoryService.categoriesOf(user.getId());
    expectedList.forEach(index -> {
      shuffled.add(categories.get(index));
    });

    categoryService.shuffle(shuffled, user.getId());
    StringBuilder actual = new StringBuilder();
    categoryService.categoriesOf(user.getId())
        .forEach(categoryDto -> {
              if (categoryDto.getSequence() != 0) {
                actual.append(categoryDto.getName());
              }
            }
        );

    assertThat(actual.toString()).isEqualTo(expected);
  }

  @Test
  public void shuffle_주어진_리스트가_원본_카테고리의_수와_같지않으면_예외를_발생한다() throws Exception {
    User user = userRepository.findByUid("tu1").get();
    categoryService.createDefault(user.getId());
    for (int sequence = 1; sequence < 5; sequence++) {
      categoryService.create(new CreateCategoryRequestDto(Integer.toString(sequence)),
          user.getId());
    }

    assertThatThrownBy(() -> {
      categoryService.shuffle(List.of(new CategoryDto(1L, "test", 1)), user.getId());
    }).isInstanceOf(ChangeCategoryWrongLengthException.class);
  }
}