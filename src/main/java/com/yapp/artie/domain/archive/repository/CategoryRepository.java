package com.yapp.artie.domain.archive.repository;

import com.yapp.artie.domain.archive.domain.category.Category;
import com.yapp.artie.domain.archive.dto.cateogry.CategoryDto;
import com.yapp.artie.domain.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  @Query("select new com.yapp.artie.domain.archive.dto.cateogry.CategoryDto(c.id, c.name, c.sequence) from Category c where c.user = :user")
  List<CategoryDto> findCategoryDto(@Param("user") User user);

  @EntityGraph(attributePaths = {"user"})
  Category findCategoryEntityGraphById(Long id);

  int countCategoriesByUser(@Param("user") User user);
}

