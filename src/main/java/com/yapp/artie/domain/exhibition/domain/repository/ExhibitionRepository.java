package com.yapp.artie.domain.exhibition.domain.repository;

import com.yapp.artie.domain.category.domain.Category;
import com.yapp.artie.domain.exhibition.domain.dto.exhibition.ExhibitionByDateResponse;
import com.yapp.artie.domain.exhibition.domain.dto.exhibition.ExhibitionByDayQueryResponse;
import com.yapp.artie.domain.exhibition.domain.dto.exhibition.ExhibitionDraftResponse;
import com.yapp.artie.domain.exhibition.domain.entity.exhibition.Exhibition;
import com.yapp.artie.domain.exhibition.domain.entity.exhibition.PinType;
import com.yapp.artie.domain.user.adapter.out.persistence.UserJpaEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {

  @Query("select count(e.id) from Exhibition e "
      + "where e.user = :user "
      + "and  e.publication.isPublished = true")
  int countExhibition(@Param("user") UserJpaEntity user);

  @EntityGraph(attributePaths = {"user"})
  Optional<Exhibition> findExhibitionEntityGraphById(Long id);

  @EntityGraph(attributePaths = {"user", "category"})
  Optional<Exhibition> findDetailExhibitionEntityGraphById(Long id);

  @Query("select new com.yapp.artie.domain.exhibition.domain.dto.exhibition."
      + "ExhibitionDraftResponse(e.id, e.contents.name, e.contents.date, e.contents.attachedLink, e.publication.isPublished) "
      + "from Exhibition e where e.user = :user and e.publication.isPublished = false")
  List<ExhibitionDraftResponse> findDraftExhibitions(@Param("user") UserJpaEntity user);

  @Query(
      value = "select e from Exhibition e "
          + "where e.user = :user "
          + "and e.category = :category "
          + "and e.publication.isPublished = true",
      countQuery = "select count(e.id) from Exhibition e "
          + "where e.publication.isPublished = true "
          + "and e.category = :category"
  )
  Page<Exhibition> findExhibitionsByCategoryAsPage(Pageable pageable,
      @Param("user") UserJpaEntity user,
      @Param("category") Category category);

  @Query(
      value = "select e from Exhibition e "
          + "where e.user = :user "
          + "and e.publication.isPublished = true",
      countQuery = "select count(e.id) from Exhibition e "
          + "where e.publication.isPublished = true "
          + "and e.user = :user"
  )
  Page<Exhibition> findExhibitionsAsPage(Pageable pageable, @Param("user") UserJpaEntity user);

  @Query(value = "SELECT p.calenderDate, p.postId, p.postNum, image. `uri` FROM "
      + "( SELECT DATE(created_at) as calenderDate, MAX(id) AS postId, count(*) postNum FROM post "
      + "WHERE created_at BETWEEN :start AND :end AND user_id = :user_id AND is_published = true "
      + "GROUP BY calenderDate ORDER BY calenderDate ASC) AS p "
      + "LEFT OUTER JOIN image ON p.postId = image.post_id AND image.is_main_image = TRUE"
      , nativeQuery = true)
  List<ExhibitionByDayQueryResponse> findExhibitionsByDay(@Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end, @Param("user_id") Long userId);

  @Query("SELECT e FROM Exhibition e WHERE e.pinType IN :types AND e.category = :category")
  Optional<Exhibition> findPinnedExhibitionWithCategory(Category category, PinType[] types);

  @Query("SELECT e FROM Exhibition e WHERE e.pinType IN :types")
  Optional<Exhibition> findPinnedExhibition(PinType[] types);

  @Modifying(clearAutomatically = true)
  @Query("update Exhibition e set e.createdAt = :createdAt where e.id = :exhibitionId")
  void updateExhibitionCreatedAt(@Param("createdAt") LocalDateTime createdAt,
      @Param("exhibitionId") Long exhibitionId);

  @Query(
      "SELECT NEW com.yapp.artie.domain.exhibition.domain.dto.exhibition.ExhibitionByDateResponse(e.id, e.contents.name) "
          + "FROM Exhibition e "
          + "WHERE e.createdAt BETWEEN :start AND :end "
          + "AND e.user = :user AND e.publication.isPublished = true "
          + "ORDER BY e.createdAt DESC")
  List<ExhibitionByDateResponse> findExhibitionsByDate(@Param("user") UserJpaEntity user,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end);

  @Query("select count(e.id) from Exhibition e "
      + "where e.category = :category "
      + "and e.publication.isPublished = true")
  int countExhibitionByCategory(@Param("category") Category category);
}