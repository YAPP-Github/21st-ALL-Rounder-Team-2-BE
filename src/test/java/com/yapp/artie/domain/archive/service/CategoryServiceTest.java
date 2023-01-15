package com.yapp.artie.domain.archive.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.yapp.artie.domain.archive.domain.category.Category;
import com.yapp.artie.domain.archive.dto.cateogry.CreateCategoryRequestDto;
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
  public void create_카테고리를_생성한다() throws Exception {
    User user = userRepository.findByUid("tu1").get();
    CreateCategoryRequestDto createCategoryRequestDto = new CreateCategoryRequestDto("test");
    Long created = categoryService.create(createCategoryRequestDto, user.getId());
    Category find = em.find(Category.class, created);
    assertThat(find.getId()).isEqualTo(created);
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
    Category category = Category.create(user, "test", 1);
    em.persist(category);

    categoryService.delete(category.getId(), user.getId());

    Category find = em.find(Category.class, category.getId());
    assertThat(Optional.ofNullable(find)).isNotPresent();
  }

}