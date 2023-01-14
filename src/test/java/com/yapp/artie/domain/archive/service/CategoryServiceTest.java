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
    CreateCategoryRequestDto createCategoryRequestDto = new CreateCategoryRequestDto("test");
    Long created = categoryService.create(createCategoryRequestDto, 1L);
    Category find = em.find(Category.class, created);
    assertThat(find.getId()).isEqualTo(created);
  }


}