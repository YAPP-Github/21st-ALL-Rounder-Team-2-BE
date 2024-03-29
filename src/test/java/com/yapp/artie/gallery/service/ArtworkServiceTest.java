package com.yapp.artie.gallery.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.yapp.artie.category.domain.Category;
import com.yapp.artie.category.domain.CategoryRepository;
import com.yapp.artie.category.dto.CreateCategoryRequest;
import com.yapp.artie.category.service.CategoryService;
import com.yapp.artie.gallery.domain.entity.artwork.Artwork;
import com.yapp.artie.gallery.domain.entity.exhibition.Exhibition;
import com.yapp.artie.gallery.domain.repository.ArtworkRepository;
import com.yapp.artie.gallery.domain.repository.ExhibitionRepository;
import com.yapp.artie.gallery.dto.artwork.CreateArtworkRequest;
import com.yapp.artie.user.adapter.out.persistence.UserJpaEntity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
  ExhibitionRepository exhibitionRepository;

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  ArtworkRepository artworkRepository;

  @Autowired
  CategoryService categoryService;

  @Autowired
  ArtworkService artworkService;

  UserJpaEntity createUser(String name, String uid) {
    UserJpaEntity user = new UserJpaEntity();
    user.setName(name);
    user.setUid(uid);
    em.persist(user);
    categoryService.create(new CreateCategoryRequest("test"), user.getId());

    return user;
  }

  @Test
  @DisplayName("정상 작품 등록")
  public void create_정상_작품_등록() throws Exception {
    UserJpaEntity user = createUser("user", "tu");
    List<Category> categoriesByUser = categoryRepository.findCategoriesByUser(user);
    Category defaultCategory = categoryService.findCategoryWithUser(
        categoriesByUser.get(0).getId(), user.getId());
    Exhibition exhibition = exhibitionRepository.save(
        Exhibition.create("test", LocalDate.now(), defaultCategory, user, null));
    List<String> tags = new ArrayList<>();
    tags.add("sample-tag");

    Long artworkId = artworkService.create(
        new CreateArtworkRequest(exhibition.getId(), "sample-uri", "sample-artist",
            "sample-name",
            tags), user.getId());

    Optional<Artwork> artwork = artworkRepository.findById(artworkId);
    assertThat(artwork.isPresent()).isTrue();
    assertThat(artwork.get().getExhibition()).isEqualTo(exhibition);
    assertThat(artwork.get().getContents().getUri()).isEqualTo("sample-uri");
    assertThat(artwork.get().getContents().getArtist()).isEqualTo("sample-artist");
    assertThat(artwork.get().getContents().getName()).isEqualTo("sample-name");
    assertThat(artwork.get().isMain()).isTrue();
    assertThat(exhibition.isPublished()).isTrue();
  }

  @Test
  @DisplayName("태그 없이도 정상 작품 등록")
  public void create_태그_없이_등록() throws Exception {
    UserJpaEntity user = createUser("user", "tu");
    List<Category> categoriesByUser = categoryRepository.findCategoriesByUser(user);
    Category defaultCategory = categoryService.findCategoryWithUser(
        categoriesByUser.get(0).getId(), user.getId());
    Exhibition exhibition = exhibitionRepository.save(
        Exhibition.create("test", LocalDate.now(), defaultCategory, user, null));

    Long artworkId = artworkService.create(
        new CreateArtworkRequest(exhibition.getId(), "sample-uri", "sample-artist",
            "sample-name",
            null), user.getId());

    Optional<Artwork> artwork = artworkRepository.findById(artworkId);
    assertThat(artwork.isPresent()).isTrue();
    assertThat(artwork.get().getExhibition()).isEqualTo(exhibition);
    assertThat(artwork.get().getContents().getUri()).isEqualTo("sample-uri");
    assertThat(artwork.get().getContents().getArtist()).isEqualTo("sample-artist");
    assertThat(artwork.get().getContents().getName()).isEqualTo("sample-name");
    assertThat(artwork.get().isMain()).isTrue();
    assertThat(exhibition.isPublished()).isTrue();
  }

  @Test
  @DisplayName("빌더로 작품 등록")
  public void create_빌더로_등록() throws Exception {
    UserJpaEntity user = createUser("user", "tu");
    List<Category> categoriesByUser = categoryRepository.findCategoriesByUser(user);
    Category defaultCategory = categoryService.findCategoryWithUser(
        categoriesByUser.get(0).getId(), user.getId());
    Exhibition exhibition = exhibitionRepository.save(
        Exhibition.create("test", LocalDate.now(), defaultCategory, user, null));
    List<String> tags = new ArrayList<>();
    tags.add("sample-tag");

    Long artworkId = artworkService.create(
        CreateArtworkRequest.builder()
            .postId(exhibition.getId())
            .imageUri("sample-uri")
            .tags(tags).build()
        , user.getId());

    Optional<Artwork> artwork = artworkRepository.findById(artworkId);
    assertThat(artwork.isPresent()).isTrue();
    assertThat(artwork.get().getExhibition()).isEqualTo(exhibition);
    assertThat(artwork.get().getContents().getUri()).isEqualTo("sample-uri");
    assertThat(artwork.get().getContents().getArtist()).isEqualTo(null);
    assertThat(artwork.get().getContents().getName()).isEqualTo(null);
    assertThat(artwork.get().isMain()).isTrue();
    assertThat(exhibition.isPublished()).isTrue();
  }

  @Test
  @DisplayName("전시에 등록되는 첫 작품은 대표 작품으로 설정")
  public void create_첫_작품_대표_작품_설정() {
    UserJpaEntity user = createUser("user", "tu");
    List<Category> categoriesByUser = categoryRepository.findCategoriesByUser(user);
    Category defaultCategory = categoryService.findCategoryWithUser(
        categoriesByUser.get(0).getId(), user.getId());
    Exhibition exhibition = exhibitionRepository.save(
        Exhibition.create("test", LocalDate.now(), defaultCategory, user, null));
    List<String> tags = new ArrayList<>();
    tags.add("sample-tag");

    Long artworkId1 = artworkService.create(
        new CreateArtworkRequest(exhibition.getId(), "sample-uri", "sample-artist",
            "sample-name",
            tags), user.getId());
    Long artworkId2 = artworkService.create(
        CreateArtworkRequest.builder()
            .postId(exhibition.getId())
            .imageUri("sample-uri").build()
        , user.getId());

    Optional<Artwork> artwork1 = artworkRepository.findById(artworkId1);
    Optional<Artwork> artwork2 = artworkRepository.findById(artworkId2);
    assertThat(artwork1.isPresent()).isTrue();
    assertThat(artwork2.isPresent()).isTrue();
    assertThat(artwork1.get().isMain()).isTrue();
    assertThat(artwork2.get().isMain()).isFalse();
    assertThat(artwork1.get().getExhibition()).isEqualTo(exhibition);
    assertThat(artwork2.get().getExhibition()).isEqualTo(exhibition);
  }

  @Test
  @DisplayName("정상 다중 작품 등록")
  public void createBatch_정상_다중_작품_등록() {
    UserJpaEntity user = createUser("user", "tu");
    List<Category> categoriesByUser = categoryRepository.findCategoriesByUser(user);
    Category defaultCategory = categoryService.findCategoryWithUser(
        categoriesByUser.get(0).getId(), user.getId());
    Exhibition exhibition = exhibitionRepository.save(
        Exhibition.create("test", LocalDate.now(), defaultCategory, user, null));
    List<String> uriList = new ArrayList<>();
    uriList.add("sample-uri-1");
    uriList.add("sample-uri-2");

    List<Long> artworkIdList = artworkService.createBatch(uriList, exhibition.getId(),
        user.getId());

    Optional<Artwork> artwork1 = artworkRepository.findById(artworkIdList.get(0));
    Optional<Artwork> artwork2 = artworkRepository.findById(artworkIdList.get(1));
    assertThat(artworkIdList.size()).isEqualTo(2);
    assertThat(artwork1.isPresent()).isTrue();
    assertThat(artwork2.isPresent()).isTrue();
    assertThat(artwork1.get().getExhibition()).isEqualTo(exhibition);
    assertThat(artwork2.get().getExhibition()).isEqualTo(exhibition);
    assertThat(artwork1.get().isMain()).isTrue();
    assertThat(artwork1.get().getContents().getUri()).isEqualTo("sample-uri-1");
    assertThat(exhibition.isPublished()).isTrue();
  }

  @Test
  @DisplayName("대표 작품을 삭제하였을 경우, 자동으로 다른 대표 작품을 설정해야합니다")
  public void delete_대표_작품_삭제() {
    UserJpaEntity user = createUser("user", "tu");
    List<Category> categoriesByUser = categoryRepository.findCategoriesByUser(user);
    Category defaultCategory = categoryService.findCategoryWithUser(
        categoriesByUser.get(0).getId(), user.getId());
    Exhibition exhibition = exhibitionRepository.save(
        Exhibition.create("test", LocalDate.now(), defaultCategory, user, null));
    List<Artwork> artworks = artworkRepository.saveAll(
        Arrays.asList(
            Artwork.create(exhibition, true, "sample-uri"),
            Artwork.create(exhibition, false, "sample-uri"),
            Artwork.create(exhibition, false, "sample-uri")));
    em.clear();

    artworkService.delete(artworks.get(0).getId(), user.getId());

    List<Artwork> newArtworks = artworkRepository.findArtworksByExhibitionOrderByCreatedAtDescIdDesc(
        exhibition);
    assertThat(newArtworks.size()).isEqualTo(2);
    assertThat(newArtworks.get(1).getId()).isEqualTo(artworks.get(1).getId());
    assertThat(newArtworks.get(1).isMain()).isTrue();
    assertThat(newArtworks.get(0).getId()).isEqualTo(artworks.get(2).getId());
    assertThat(newArtworks.get(0).isMain()).isFalse();
  }

  @Test
  @DisplayName("대표 작품이었던 작품에 대해 대표 작품을 설정하려는 요청이 있어도, 정상적으로 대표 작품 상태를 유지해야합니다.")
  public void setMainArtwork_이미_대표_작품을_대표_작품으로_설정() {
    UserJpaEntity user = createUser("user", "tu");
    List<Category> categoriesByUser = categoryRepository.findCategoriesByUser(user);
    Category defaultCategory = categoryService.findCategoryWithUser(
        categoriesByUser.get(0).getId(), user.getId());
    Exhibition exhibition = exhibitionRepository.save(
        Exhibition.create("test", LocalDate.now(), defaultCategory, user, null));
    artworkRepository.save(Artwork.create(exhibition, true, "sample-uri")).getId();
    em.flush();
    em.clear();

    artworkService.setMainArtwork(1L, user.getId());
    em.flush();
    em.clear();

    Artwork updatedArtwork = em.find(Artwork.class, 1L);

    assertThat(updatedArtwork.isMain()).isTrue();
  }
}

