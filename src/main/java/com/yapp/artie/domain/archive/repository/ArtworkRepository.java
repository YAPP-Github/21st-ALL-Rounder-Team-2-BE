package com.yapp.artie.domain.archive.repository;

import com.yapp.artie.domain.archive.domain.artwork.Artwork;
import com.yapp.artie.domain.archive.domain.exhibit.Exhibit;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Long> {

  Long countArtworkByExhibitId(Long id);

  @Query(
      "select e from Artwork e where e.exhibit = :exhibit and e.isMain = true "
  )
  Optional<Artwork> findMainArtworkByExhibitId(@Param("exhibit") Exhibit exhibit);
}
