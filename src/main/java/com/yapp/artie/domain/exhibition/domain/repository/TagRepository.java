package com.yapp.artie.domain.exhibition.domain.repository;

import com.yapp.artie.domain.exhibition.domain.entity.artwork.Artwork;
import com.yapp.artie.domain.exhibition.domain.entity.artwork.Tag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

  List<Tag> findAllByArtworkOrderBySequenceAsc(Artwork artwork);

  List<Tag> findAllByArtwork(Artwork artwork);

  List<Tag> findAllByName(String name);
}

