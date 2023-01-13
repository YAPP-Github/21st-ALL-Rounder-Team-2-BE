package com.yapp.artie.domain.archive.repository;

import com.yapp.artie.domain.archive.domain.tag.Tag;
import com.yapp.artie.domain.archive.dto.tag.TagDto;
import com.yapp.artie.domain.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

  @Query("select new com.yapp.artie.domain.archive.dto.tag.TagDto(t.id, t.name) from Tag t where t.user =:user")
  List<TagDto> findTagDto(@Param("user") User user);
}

