package com.yapp.artie.category.controller;

import com.yapp.artie.category.dto.CategoryDetailResponse;
import com.yapp.artie.category.dto.CreateCategoryRequest;
import com.yapp.artie.category.dto.CreateCategoryResponse;
import com.yapp.artie.category.dto.UpdateCategoryRequest;
import com.yapp.artie.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/category")
@RestController
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  @Operation(summary = "카테고리 조회", description = "사용자 카테고리 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "카테고리가 성공적으로 조회됨",
          content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CategoryDetailResponse.class)))),
  })
  @GetMapping()
  public ResponseEntity<List<CategoryDetailResponse>> getCategories(Authentication authentication) {
    Long userId = Long.parseLong(authentication.getName());
    List<CategoryDetailResponse> categories = categoryService.categoriesOf(userId);

    return ResponseEntity.ok(categories);
  }

  @Operation(summary = "카테고리 생성", description = "사용자 카테고리 생성")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "카테고리가 성공적으로 생성됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateCategoryResponse.class))),
  })
  @PostMapping()
  public ResponseEntity<CreateCategoryResponse> createCategories(Authentication authentication,
      @RequestBody CreateCategoryRequest createCategoryRequest) {
    Long userId = Long.parseLong(authentication.getName());
    Long id = categoryService.create(createCategoryRequest, userId);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new CreateCategoryResponse(id));
  }

  @Operation(summary = "카테고리 수정", description = "카테고리 수정")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "카테고리가 성공적으로 수정됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))),
  })
  @PutMapping("/{id}")
  public ResponseEntity<? extends HttpEntity> updateCategory(Authentication authentication,
      @PathVariable("id") Long id, @RequestBody
  UpdateCategoryRequest updateCategoryRequest) {
    Long userId = Long.parseLong(authentication.getName());
    categoryService.update(updateCategoryRequest, id, userId);

    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "카테고리 삭제", description = "사용자 카테고리 삭제")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "카테고리가 성공적으로 삭제됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))),
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<? extends HttpEntity> deleteCategory(Authentication authentication,
      @PathVariable("id") Long id) {
    Long userId = Long.parseLong(authentication.getName());
    categoryService.delete(id, userId);

    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "카테고리 순서 변경", description = "카테고리 순서를 변경합니다. "
      + "변경을 원하는 순서로 정렬시켜 배열에 담아 전달해주세요. "
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "카테고리 순서가 성공적으로 수정됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))),
  })
  @PutMapping("/sequence")
  public ResponseEntity<? extends HttpEntity> updateCategorySequence(
      Authentication authentication, @RequestBody CategoryDetailResponse[] changedCategoryDetails) {
    Long userId = Long.parseLong(authentication.getName());
    categoryService.shuffle(List.of(changedCategoryDetails), userId);

    return ResponseEntity.noContent().build();
  }
}
