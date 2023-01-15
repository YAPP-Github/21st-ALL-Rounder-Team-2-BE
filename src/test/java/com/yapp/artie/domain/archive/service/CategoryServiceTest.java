package com.yapp.artie.domain.archive.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.yapp.artie.domain.archive.domain.category.Category;
import com.yapp.artie.domain.archive.dto.cateogry.CreateCategoryRequestDto;
import com.yapp.artie.domain.archive.exception.CategoryAlreadyExistException;
import com.yapp.artie.domain.archive.exception.CategoryNotFoundException;
import com.yapp.artie.domain.archive.exception.ChangeDefaultCategoryException;
import com.yapp.artie.domain.archive.exception.ExceededCategoryCountException;
import com.yapp.artie.domain.archive.exception.NotOwnerOfCategoryException;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.repository.UserRepository;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class CategoryServiceTest {

  @Autowired
  EntityManager em;

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

}