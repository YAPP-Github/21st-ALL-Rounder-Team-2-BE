package com.yapp.artie.domain.archive.repository;

import com.yapp.artie.domain.archive.domain.category.Category;
import com.yapp.artie.domain.archive.domain.exhibit.Exhibit;
import com.yapp.artie.domain.archive.domain.exhibit.PinType;
import com.yapp.artie.domain.archive.dto.exhibit.CalenderQueryResultDto;
import com.yapp.artie.domain.archive.dto.exhibit.PostInfoDto;
import com.yapp.artie.domain.user.domain.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitRepository extends JpaRepository<Exhibit, Long> {

  @EntityGraph(attributePaths = {"user"})
  Optional<Exhibit> findExhibitEntityGraphById(Long id);

  @EntityGraph(attributePaths = {"user", "category"})
  Optional<Exhibit> findDetailExhibitEntityGraphById(Long id);

  @Query("select new com.yapp.artie.domain.archive.dto.exhibit."
      + "PostInfoDto(e.id, e.contents.name, e.contents.date, e.publication.isPublished) "
      + "from Exhibit e where e.user = :user and e.publication.isPublished = false")
  List<PostInfoDto> findDraftExhibitDto(@Param("user") User user);

  @Query(
      value = "select e from Exhibit e "
          + "where e.user = :user "
          + "and e.category = :category "
          + "and e.publication.isPublished = true",
      countQuery = "select count(e.id) from Exhibit e"
  )
  Page<Exhibit> findCategoryExhibitPageBy(Pageable pageable, @Param("user") User user,
      @Param("category") Category category);

  @Query(
      value = "select e from Exhibit e "
          + "where e.user = :user "
          + "and e.publication.isPublished = true",
      countQuery = "select count(e.id) from Exhibit e"
  )
  Page<Exhibit> findAllExhibitPageBy(Pageable pageable, @Param("user") User user);

  @Query(value = " SELECT p.post_date as date, p.post_id, p.post_num, image. `uri` FROM "
      + "( SELECT post_date, min(id) AS post_id, count(*) post_num FROM post "
      + "WHERE post_date BETWEEN :start AND :end AND user_id = :user_id "
      + "GROUP BY post_date ORDER BY post_date ASC) AS p "
      + "LEFT OUTER JOIN image ON p.post_id = image.post_id AND image.is_main_image = TRUE"
      , nativeQuery = true)
  List<CalenderQueryResultDto> findExhibitAsCalenderByDay(@Param("start") LocalDate start,
      @Param("end") LocalDate end, @Param("user_id") Long userId);

  @Query("SELECT e FROM Exhibit e WHERE e.pinType IN :types AND e.category = :category")
  Optional<Exhibit> findPinnedExhibitWithCategory(Category category, PinType[] types);

  @Query("SELECT e FROM Exhibit e WHERE e.pinType IN :types")
  Optional<Exhibit> findPinnedExhibit(PinType[] types);
}