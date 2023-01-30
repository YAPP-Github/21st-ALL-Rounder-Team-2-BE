package com.yapp.artie.domain.archive.repository;

import com.yapp.artie.domain.archive.domain.artwork.Artwork;
import com.yapp.artie.domain.archive.domain.tag.ArtworkTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtworkTagRepository extends JpaRepository<ArtworkTag, Long> {

  List<ArtworkTag> findArtworkTagsByArtworkOrderBySequenceAsc(Artwork artwork);

  List<ArtworkTag> findArtworkTagsByArtwork(Artwork artwork);
}

