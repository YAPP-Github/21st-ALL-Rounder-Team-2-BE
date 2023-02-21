package com.yapp.artie.domain.archive.repository;

import com.yapp.artie.domain.archive.domain.category.Category;
import com.yapp.artie.domain.archive.dto.cateogry.CategoryDto;
import com.yapp.artie.domain.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  @Query("select new com.yapp.artie.domain.archive.dto.cateogry.CategoryDto(c.id, c.name, c.sequence) from Category c where c.user = :user order by c.sequence")
  List<CategoryDto> findCategoryDto(@Param("user") User user);

  @Modifying(clearAutomatically = true)
  @Query("update Category c set c.sequence = c.sequence - 1 where c.sequence > :sequence and c.user = :user")
  void bulkSequenceMinus(@Param("user") User user, @Param("sequence") int sequence);

  @EntityGraph(attributePaths = {"user"})
  Category findCategoryEntityGraphById(Long id);

  int countCategoriesByUser(@Param("user") User user);

  List<Category> findCategoriesByUserOrderBySequence(User user);

  @Modifying(clearAutomatically = true)
  void deleteAllByUser(User user);
}

