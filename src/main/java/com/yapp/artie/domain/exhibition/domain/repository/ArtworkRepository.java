package com.yapp.artie.domain.exhibition.domain.repository;

import com.yapp.artie.domain.exhibition.domain.entity.artwork.Artwork;
import com.yapp.artie.domain.exhibition.domain.entity.exhibition.Exhibition;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Long> {

  Long countArtworkByExhibitionId(Long id);

  @Query(
      "select e from Artwork e where e.exhibition = :exhibition and e.isMain = true "
  )
  Optional<Artwork> findMainArtworkByExhibitionId(@Param("exhibition") Exhibition exhibition);

  @Query(
      value = "select a from Artwork a "
          + "where a.exhibition = :exhibition ",
      countQuery = "select count(a.id) from Artwork a"
  )
  Page<Artwork> findAllArtworkAsPage(Pageable pageable, @Param("exhibition") Exhibition exhibition);

  List<Artwork> findArtworksByExhibitionOrderByCreatedAtDescIdDesc(Exhibition exhibition);

  @Modifying
  @Query("update Artwork a set a.isMain = false where a.exhibition = :exhibition")
  void updateArtworkNotMainByExhibition(@Param("exhibition") Exhibition exhibition);

  @Query("select a from Artwork a where a.exhibition = :exhibition and a.id <> :deletedId order by a.createdAt, a.id")
  List<Artwork> findFirstByExhibitionWithoutDeleted(@Param("exhibition") Exhibition exhibition,
      @Param("deletedId") Long deletedId);
}
