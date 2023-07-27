package com.yapp.artie.category.domain;

import com.yapp.artie.category.dto.CategoryDetailResponse;
import com.yapp.artie.user.adapter.out.persistence.UserJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

  @Query("select new com.yapp.artie.category.dto.CategoryDetailResponse(c.id, c.name, c.sequence) from Category c where c.user = :user order by c.sequence")
  List<CategoryDetailResponse> findCategoryDto(@Param("user") UserJpaEntity user);

  @Modifying(clearAutomatically = true)
  @Query("update Category c set c.sequence = c.sequence - 1 where c.sequence > :sequence and c.user = :user")
  void bulkSequenceMinus(@Param("user") UserJpaEntity user, @Param("sequence") int sequence);

  @Query("select c from Category c where c.id = :id and c.user.id = :userId")
  Optional<Category> findUserCategory(@Param("id") Long id, @Param("userId") Long userId);

  int countCategoriesByUser(@Param("user") UserJpaEntity user);

  List<Category> findCategoriesByUserOrderBySequence(UserJpaEntity user);

  @Modifying(clearAutomatically = true)
  void deleteAllByUser(UserJpaEntity user);

  List<Category> findCategoriesByUser(UserJpaEntity userJpaEntity);
}
