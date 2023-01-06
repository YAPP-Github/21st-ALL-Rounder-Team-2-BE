package com.yapp.artie.domain.archive.controller;


import com.yapp.artie.domain.archive.dto.cateogry.CategoryDto;
import com.yapp.artie.domain.archive.dto.cateogry.CreateCategoryRequestDto;
import com.yapp.artie.domain.archive.dto.cateogry.CreateCategoryResponseDto;
import com.yapp.artie.domain.archive.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/category")
@RestController
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping("/")
  public ResponseEntity<List<CategoryDto>> getCategories(Authentication authentication) {
    Long userId = Long.parseLong(authentication.getName());
    List<CategoryDto> categories = categoryService.categories(userId);
    return ResponseEntity.ok(categories);
  }

  @PostMapping("/")
  public ResponseEntity<CreateCategoryResponseDto> createCategories(Authentication authentication,
      @RequestBody CreateCategoryRequestDto createCategoryRequestDto) {
    Long userId = Long.parseLong(authentication.getName());
    Long id = categoryService.create(createCategoryRequestDto, userId);

    return ResponseEntity.ok(new CreateCategoryResponseDto(id));
  }

}
