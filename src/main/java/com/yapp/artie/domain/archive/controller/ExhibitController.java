package com.yapp.artie.domain.archive.controller;


import com.yapp.artie.domain.archive.dto.exhibit.CreateExhibitRequestDto;
import com.yapp.artie.domain.archive.dto.exhibit.CreateExhibitResponseDto;
import com.yapp.artie.domain.archive.dto.exhibit.PostInfoDto;
import com.yapp.artie.domain.archive.service.ExhibitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/post")
@RestController
@RequiredArgsConstructor
public class ExhibitController {

  private final ExhibitService exhibitService;

  @Operation(summary = "전시 조회", description = "특정 전시 정보 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "전시가 성공적으로 조회됌",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostInfoDto.class))),
  })
  @GetMapping("/{id}")
  public ResponseEntity<PostInfoDto> getPost(Authentication authentication,
      @PathVariable("id") Long id) {
    Long userId = Long.parseLong(authentication.getName());
    PostInfoDto exhibitInformation = exhibitService.getExhibitInformation(id, userId);

    return ResponseEntity.ok().body(exhibitInformation);
  }

  @Operation(summary = "전시 생성", description = "전시 생성")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "전시가 성공적으로 생성됌",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateExhibitResponseDto.class))),
  })
  @PostMapping()
  public ResponseEntity<CreateExhibitResponseDto> createPost(Authentication authentication,
      @RequestBody
      CreateExhibitRequestDto createExhibitRequestDto) {
    Long userId = Long.parseLong(authentication.getName());
    Long id = exhibitService.create(createExhibitRequestDto, userId);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new CreateExhibitResponseDto(id));
  }
}
