package com.yapp.artie.domain.user.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.yapp.artie.BaseIntegrationTest;
import com.yapp.artie.domain.archive.domain.artwork.Artwork;
import com.yapp.artie.domain.archive.domain.category.Category;
import com.yapp.artie.domain.archive.domain.exhibit.Exhibit;
import com.yapp.artie.domain.archive.domain.tag.Tag;
import com.yapp.artie.domain.archive.repository.ArtworkRepository;
import com.yapp.artie.domain.archive.repository.CategoryRepository;
import com.yapp.artie.domain.archive.repository.ExhibitRepository;
import com.yapp.artie.domain.archive.repository.TagRepository;
import com.yapp.artie.domain.user.adapter.out.persistence.UserJpaEntity;
import com.yapp.artie.domain.user.domain.UserTest;
import com.yapp.artie.domain.user.repository.UserRepository;
import com.yapp.artie.global.authentication.JwtServiceImpl;
import java.time.LocalDate;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class UserControllerTest extends BaseIntegrationTest {

  @Autowired
  EntityManager em;

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

  @MockBean
  JwtServiceImpl jwtService;

  // TODO : 헤더 토큰 ( Authentication ) 모킹 필요. 현재는 getUserId 메소드에 의존하고 있음
  @Test
  @DisplayName("유저 삭제(탈퇴) API - 유저 데이터가 삭제되면 해당 유저의 카테고리, 전시, 작품, 태그 데이터가 함께 삭제되어야합니다.")
  public void deleteUser() throws Exception {
    UserJpaEntity user = userRepository.save(UserTest.TEST_SAVED_USER);
    Category category = categoryRepository.save(Category.create(user, "category-name", 1));
    Exhibit exhibit = exhibitRepository.save(
        Exhibit.create("exhibit-name", LocalDate.now(), category, user, "exhibit-link"));
    Artwork artwork = artworkRepository.save(Artwork.create(exhibit, true, "artwork-uri"));
    Tag tag = tagRepository.save(new Tag(user, artwork, 1, "tag-name"));
    em.clear();

    doNothing().when(jwtService).withdraw(anyString());

    mvc.perform(delete("/user"))
        .andExpect(status().isNoContent())
        .andDo(print());

    em.flush();
    assertThat(userRepository.findById(user.getId()).isPresent()).isFalse();
    assertThat(categoryRepository.findById(category.getId()).isPresent()).isFalse();
    assertThat(exhibitRepository.findById(exhibit.getId()).isPresent()).isFalse();
    assertThat(artworkRepository.findById(artwork.getId()).isPresent()).isFalse();
    assertThat(tagRepository.findById(tag.getId()).isPresent()).isFalse();
  }
}