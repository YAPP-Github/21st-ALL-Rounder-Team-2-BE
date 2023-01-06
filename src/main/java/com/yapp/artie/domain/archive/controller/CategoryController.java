package com.yapp.artie.domain.archive.controller;


import com.yapp.artie.domain.archive.dto.CategoryDto;
import com.yapp.artie.domain.archive.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
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
}
