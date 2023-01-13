package com.yapp.artie.domain.archive.repository;

import com.yapp.artie.domain.archive.domain.artwork.Artwork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtworkRepository extends JpaRepository<Artwork, Long> {

  Long countArtworkByExhibitId(Long id);
}
