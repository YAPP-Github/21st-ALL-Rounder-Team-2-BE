package com.yapp.artie.domain.archive.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.yapp.artie.domain.archive.domain.artwork.Artwork;
import com.yapp.artie.domain.archive.domain.category.Category;
import com.yapp.artie.domain.archive.domain.exhibit.Exhibit;
import com.yapp.artie.domain.archive.dto.artwork.CreateArtworkRequestDto;
import com.yapp.artie.domain.archive.dto.cateogry.CreateCategoryRequestDto;
import com.yapp.artie.domain.archive.dto.tag.CreateArtworkTagDto;
import com.yapp.artie.domain.archive.repository.ArtworkRepository;
import com.yapp.artie.domain.archive.repository.CategoryRepository;
import com.yapp.artie.domain.archive.repository.ExhibitRepository;
import com.yapp.artie.domain.user.domain.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class ArtworkServiceTest {

  @Autowired
  EntityManager em;

  @Autowired
  ExhibitRepository exhibitRepository;

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  ArtworkRepository artworkRepository;

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
  @DisplayName("정상 작품 등록")
  public void create_정상_작품_등록() throws Exception {
    User user = createUser("user", "tu");
    Category defaultCategory = categoryRepository.findCategoryEntityGraphById(user.getId());
    Exhibit exhibit = exhibitRepository.save(
        Exhibit.create("test", LocalDate.now(), defaultCategory, user, null));
    List<CreateArtworkTagDto> tags = new ArrayList<>();
    tags.add(new CreateArtworkTagDto(null, "sample-tag"));

    Long artworkId = artworkService.create(
        new CreateArtworkRequestDto(exhibit.getId(), "sample-uri", "sample-artist", "sample-name",
            tags), user.getId());

    Optional<Artwork> artwork = artworkRepository.findById(artworkId);
    assertThat(artwork.isPresent()).isTrue();
    assertThat(artwork.get().getExhibit()).isEqualTo(exhibit);
    assertThat(artwork.get().getContents().getUri()).isEqualTo("sample-uri");
    assertThat(artwork.get().getContents().getArtist()).isEqualTo("sample-artist");
    assertThat(artwork.get().getContents().getName()).isEqualTo("sample-name");
    assertThat(artwork.get().isMain()).isTrue();
    assertThat(exhibit.isPublished()).isTrue();
  }

  @Test
  @DisplayName("전시에 등록되는 첫 작품은 대표 작품으로 설정")
  public void create_첫_작품_대표_작품_설정() {
    User user = createUser("user", "tu");
    Category defaultCategory = categoryRepository.findCategoryEntityGraphById(user.getId());
    Exhibit exhibit = exhibitRepository.save(
        Exhibit.create("test", LocalDate.now(), defaultCategory, user, null));
    List<CreateArtworkTagDto> tags = new ArrayList<>();
    tags.add(new CreateArtworkTagDto(null, "sample-tag"));

    Long artworkId1 = artworkService.create(
        new CreateArtworkRequestDto(exhibit.getId(), "sample-uri", "sample-artist", "sample-name",
            tags), user.getId());
    Long artworkId2 = artworkService.create(
        CreateArtworkRequestDto.builder().postId(exhibit.getId()).imageUri("sample-uri")
            .tags(new ArrayList<>()).build(),
        user.getId());

    Optional<Artwork> artwork1 = artworkRepository.findById(artworkId1);
    Optional<Artwork> artwork2 = artworkRepository.findById(artworkId2);
    assertThat(artwork1.isPresent()).isTrue();
    assertThat(artwork2.isPresent()).isTrue();
    assertThat(artwork1.get().isMain()).isTrue();
    assertThat(artwork2.get().isMain()).isFalse();
    assertThat(artwork1.get().getExhibit()).isEqualTo(exhibit);
    assertThat(artwork2.get().getExhibit()).isEqualTo(exhibit);
  }

  @Test
  @DisplayName("정상 다중 작품 등록")
  public void createBatch_정상_다중_작품_등록() {
    User user = createUser("user", "tu");
    Category defaultCategory = categoryRepository.findCategoryEntityGraphById(user.getId());
    Exhibit exhibit = exhibitRepository.save(
        Exhibit.create("test", LocalDate.now(), defaultCategory, user, null));
    List<String> uriList = new ArrayList<>();
    uriList.add("sample-uri-1");
    uriList.add("sample-uri-2");

    List<Long> artworkIdList = artworkService.createBatch(uriList, exhibit.getId(), user.getId());

    Optional<Artwork> artwork1 = artworkRepository.findById(artworkIdList.get(0));
    Optional<Artwork> artwork2 = artworkRepository.findById(artworkIdList.get(1));
    assertThat(artworkIdList.size()).isEqualTo(2);
    assertThat(artwork1.isPresent()).isTrue();
    assertThat(artwork2.isPresent()).isTrue();
    assertThat(artwork1.get().getExhibit()).isEqualTo(exhibit);
    assertThat(artwork2.get().getExhibit()).isEqualTo(exhibit);
    assertThat(artwork1.get().isMain()).isTrue();
    assertThat(artwork1.get().getContents().getUri()).isEqualTo("sample-uri-1");
    assertThat(exhibit.isPublished()).isTrue();
  }
}
