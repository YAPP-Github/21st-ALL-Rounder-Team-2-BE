package com.yapp.artie.domain.archive.controller;


import com.yapp.artie.domain.archive.dto.cateogry.CategoryDto;
import com.yapp.artie.domain.archive.dto.cateogry.CreateCategoryRequestDto;
import com.yapp.artie.domain.archive.dto.cateogry.CreateCategoryResponseDto;
import com.yapp.artie.domain.archive.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    List<CategoryDto> categories = categoryService.categories(userId);
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


  @Operation(summary = "카테고리 삭제", description = "사용자 카테고리 삭제")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "카테고리가 성공적으로 삭제됌",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))),
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<? extends HttpEntity> deleteCategory(Authentication authentication,
      @PathVariable("id") Long id) {
    Long userId = Long.parseLong(authentication.getName());
    categoryService.delete(id, userId);

    return ResponseEntity.noContent().build();
  }
}
