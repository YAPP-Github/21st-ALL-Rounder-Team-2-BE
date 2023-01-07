package com.yapp.artie.domain.archive.repository;

import com.yapp.artie.domain.archive.domain.exhibit.Exhibit;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitRepository extends JpaRepository<Exhibit, Long> {

  @EntityGraph(attributePaths = {"user"})
  Exhibit findExhibitEntityGraphById(Long id);
}
