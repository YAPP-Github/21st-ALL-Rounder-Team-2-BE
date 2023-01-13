package com.yapp.artie.domain.archive.repository;

import com.yapp.artie.domain.archive.domain.category.Category;
import com.yapp.artie.domain.archive.domain.exhibit.Exhibit;
import java.util.Optional;
import com.yapp.artie.domain.archive.dto.exhibit.PostInfoDto;
import com.yapp.artie.domain.user.domain.User;
import java.util.List;
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

  @Query("select new com.yapp.artie.domain.archive.dto.exhibit."
      + "PostInfoDto(e.id, e.contents.name, e.contents.date, e.publication.isPublished) "
      + "from Exhibit e where e.user = :user and e.publication.isPublished = false")
  List<PostInfoDto> findExhibitDto(@Param("user") User user);


  @Query(
      value = "select e from Exhibit e "
          + "where e.user = :user "
          + "and e.category = :category "
          + "and e.publication.isPublished = true",
      countQuery = "select count(e.id) from Exhibit e"
  )
  Page<Exhibit> findExhibitAllCountBy(Pageable pageable, @Param("user") User user,
      @Param("category") Category category);
}
