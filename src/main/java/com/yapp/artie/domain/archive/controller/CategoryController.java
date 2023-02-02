package com.yapp.artie.domain.archive.controller;

import com.yapp.artie.domain.archive.dto.cateogry.CategoryDto;
import com.yapp.artie.domain.archive.dto.cateogry.CreateCategoryRequestDto;
import com.yapp.artie.domain.archive.dto.cateogry.CreateCategoryResponseDto;
import com.yapp.artie.domain.archive.dto.cateogry.UpdateCategoryRequestDto;
import com.yapp.artie.domain.archive.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
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
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))),
  })
  @GetMapping()
  public ResponseEntity<List<CategoryDto>> getCategories(Authentication authentication) {
    Long userId = Long.parseLong(authentication.getName());
    List<CategoryDto> categories = categoryService.categoriesOf(userId);

    return ResponseEntity.ok(categories);
  }

  @Operation(summary = "카테고리 생성", description = "사용자 카테고리 생성")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "카테고리가 성공적으로 생성됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateCategoryResponseDto.class))),
  })
  @PostMapping()
  public ResponseEntity<CreateCategoryResponseDto> createCategories(Authentication authentication,
      @RequestBody CreateCategoryRequestDto createCategoryRequestDto) {
    Long userId = Long.parseLong(authentication.getName());
    Long id = categoryService.create(createCategoryRequestDto, userId);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new CreateCategoryResponseDto(id));
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
  UpdateCategoryRequestDto updateCategoryRequestDto) {
    Long userId = Long.parseLong(authentication.getName());
    categoryService.update(updateCategoryRequestDto, id, userId);

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
      + "전체 카테고리에서 기본 카테고리를 제외하고 배열에 담아 변경을 원하는 순서로 정렬시켜 전달해야 합니다. "
      + "요청되는 카테고리에 기본 카테고리가 포함되어 있으면 정상적인 작동을 기대하기 어렵습니다. 주의 부탁드립니다."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "카테고리 순서가 성공적으로 수정됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))),
  })
  @PutMapping("/sequence")
  public ResponseEntity<? extends HttpEntity> updateCategorySequence(
      Authentication authentication, @RequestBody CategoryDto[] changeCategorySequenceDtos) {
    Long userId = Long.parseLong(authentication.getName());
    categoryService.shuffle(List.of(changeCategorySequenceDtos), userId);

    return ResponseEntity.noContent().build();
  }
}
