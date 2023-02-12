package com.yapp.artie.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.yapp.artie.domain.archive.dto.cateogry.CategoryDto;
import com.yapp.artie.domain.archive.dto.cateogry.CreateCategoryRequestDto;
import com.yapp.artie.domain.archive.dto.exhibit.CreateExhibitRequestDto;
import com.yapp.artie.domain.archive.service.CategoryService;
import com.yapp.artie.domain.archive.service.ExhibitService;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.dto.response.UserThumbnailResponseDto;
import java.time.LocalDate;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class UserThumbnailServiceTest {

  @Autowired
  EntityManager em;

  @Autowired
  ExhibitService exhibitService;

  @Autowired
  CategoryService categoryService;

  @Autowired
  UserThumbnailService userThumbnailService;

  User createUser(String name, String uid) {
    User user = new User();
    user.setName(name);
    user.setUid(uid);
    em.persist(user);
    categoryService.create(new CreateCategoryRequestDto("test"), user.getId());

    return user;
  }

  void createExhibit(User user) {
    CategoryDto defaultCategory = categoryService.categoriesOf(user.getId()).get(0);
    CreateExhibitRequestDto exhibitRequestDto = new CreateExhibitRequestDto("test",
        defaultCategory.getId(),
        LocalDate.now());

    Long created = exhibitService.create(exhibitRequestDto, user.getId());
    exhibitService.publish(created, user.getId());
  }

  @Test
  public void getUserThumbnail_사용자의_닉네임과_전시개수를_반환한다() throws Exception {
    String expectedName = "le2sky";
    int expectedCount = 10;
    User user = createUser(expectedName, "test-123");
    for (int i = 0; i < expectedCount; i++) {
      createExhibit(user);
    }

    UserThumbnailResponseDto userThumbnail = userThumbnailService.getUserThumbnail(user.getId());
    assertThat(userThumbnail.getExhibitCount()).isEqualTo(10);
    assertThat(userThumbnail.getName()).isEqualTo(expectedName);
  }
}