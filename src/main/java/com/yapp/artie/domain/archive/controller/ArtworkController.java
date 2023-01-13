package com.yapp.artie.domain.archive.controller;

import com.yapp.artie.domain.archive.dto.artwork.CreateArtworkRequestDto;
import com.yapp.artie.domain.archive.dto.artwork.CreateArtworkResponseDto;
import com.yapp.artie.domain.archive.service.ArtworkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/artwork")
@RestController
@RequiredArgsConstructor
public class ArtworkController {

  private final ArtworkService artworkService;

  @Operation(summary = "전시 작품 추가", description = "작품(이미지)를 전시에 추가")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "전시 작품이 성공적으로 추가됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateArtworkResponseDto.class))),
  })
  @PostMapping()
  public ResponseEntity<CreateArtworkResponseDto> createArtwork(Authentication authentication,
      @RequestBody
      CreateArtworkRequestDto createArtworkRequestDto) {

    Long userId = Long.parseLong(authentication.getName());
    Long id = artworkService.create(createArtworkRequestDto, userId);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new CreateArtworkResponseDto(id));
  }
}
