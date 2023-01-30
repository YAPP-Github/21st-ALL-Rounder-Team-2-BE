package com.yapp.artie.domain.archive.controller;

import com.yapp.artie.domain.archive.dto.artwork.ArtworkBrowseThumbnailDto;
import com.yapp.artie.domain.archive.dto.artwork.ArtworkInfoDto;
import com.yapp.artie.domain.archive.dto.artwork.ArtworkThumbnailDto;
import com.yapp.artie.domain.archive.dto.artwork.ArtworkThumbnailDtoPage;
import com.yapp.artie.domain.archive.dto.artwork.CreateArtworkRequestDto;
import com.yapp.artie.domain.archive.dto.artwork.CreateArtworkResponseDto;
import com.yapp.artie.domain.archive.service.ArtworkService;
import com.yapp.artie.global.exception.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
      @ApiResponse(
          responseCode = "400",
          description = "잘못된 입력",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "401",
          description = "인증 오류",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "403",
          description = "접근 불가능한 전시",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "404",
          description = "찾을 수 없는 회원",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "404",
          description = "찾을 수 없는 태그",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "409",
          description = "이미 존재하는 태그",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
  })
  @PostMapping()
  public ResponseEntity<CreateArtworkResponseDto> createArtwork(Authentication authentication,
      @RequestBody @Valid
      CreateArtworkRequestDto createArtworkRequestDto) {

    Long userId = Long.parseLong(authentication.getName());
    Long id = artworkService.create(createArtworkRequestDto, userId);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new CreateArtworkResponseDto(id));
  }

  @Operation(summary = "전시의 작품 목록 조회", description = "전시 상세 페이지의 작품 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "전시 작품 목록 조회",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtworkThumbnailDtoPage.class))),
      @ApiResponse(
          responseCode = "400",
          description = "잘못된 입력",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "401",
          description = "인증 오류",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "403",
          description = "접근 불가능한 전시",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "404",
          description = "찾을 수 없는 회원",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
  })
  @GetMapping("/post/{id}")
  public ResponseEntity<Page<ArtworkThumbnailDto>> getArtworkPageFromPost(
      Authentication authentication,
      @Parameter(name = "id", description = "전시 ID", in = ParameterIn.PATH) @Valid @PathVariable("id") Long exhibitId,
      @PageableDefault(size = 20, sort = {
          "createdAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
    Long userId = Long.parseLong(authentication.getName());
    return ResponseEntity.ok().body(artworkService.getArtworkAsPage(exhibitId, userId, pageable));
  }

  @Operation(summary = "작품 상세 정보 조회", description = "작품 상세 페이지의 작품 상세 정보 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "작품 상세 정보 조회",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtworkInfoDto.class))),
      @ApiResponse(
          responseCode = "400",
          description = "잘못된 입력",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "401",
          description = "인증 오류",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "403",
          description = "접근 불가능한 전시",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "404",
          description = "찾을 수 없는 회원",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "404",
          description = "찾을 수 없는 작품",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
  })
  @GetMapping("/{id}")
  public ResponseEntity<ArtworkInfoDto> getArtworkInfo(
      Authentication authentication,
      @Parameter(name = "id", description = "작품 ID", in = ParameterIn.PATH) @Valid @PathVariable("id") Long artworkId) {
    Long userId = Long.parseLong(authentication.getName());
    return ResponseEntity.ok().body(artworkService.getArtworkInfo(artworkId, userId));
  }

  @Operation(summary = "작품 탐색 썸네일 목록 조회", description = "작품 상세 페이지의 작품 썸네일 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "작품 썸네일 목록 조회",
          content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ArtworkBrowseThumbnailDto.class)))),
      @ApiResponse(
          responseCode = "400",
          description = "잘못된 입력",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "401",
          description = "인증 오류",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "403",
          description = "접근 불가능한 전시",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "404",
          description = "찾을 수 없는 회원",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "404",
          description = "찾을 수 없는 전시",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
  })
  @GetMapping("post/{id}/thumbnail")
  public ResponseEntity<List<ArtworkBrowseThumbnailDto>> getArtworkBrowseThumbnails(
      Authentication authentication,
      @Parameter(name = "id", description = "전시 ID", in = ParameterIn.PATH) @Valid @PathVariable("id") Long exhibitId) {

    Long userId = Long.parseLong(authentication.getName());
    return ResponseEntity.ok().body(artworkService.getArtworkBrowseThumbnail(exhibitId, userId));
  }
}
