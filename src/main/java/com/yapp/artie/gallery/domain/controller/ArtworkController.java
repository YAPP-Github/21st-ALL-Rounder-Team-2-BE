package com.yapp.artie.gallery.domain.controller;

import com.yapp.artie.gallery.domain.dto.artwork.ArtworkDetailResponse;
import com.yapp.artie.gallery.domain.dto.artwork.ArtworkImageThumbnailResponse;
import com.yapp.artie.gallery.domain.dto.artwork.ArtworkThumbnailResponse;
import com.yapp.artie.gallery.domain.dto.artwork.CreateArtworkBatchRequest;
import com.yapp.artie.gallery.domain.dto.artwork.CreateArtworkBatchResponse;
import com.yapp.artie.gallery.domain.dto.artwork.CreateArtworkRequest;
import com.yapp.artie.gallery.domain.dto.artwork.CreateArtworkResponse;
import com.yapp.artie.gallery.domain.dto.artwork.UpdateArtworkRequest;
import com.yapp.artie.gallery.domain.service.ArtworkService;
import com.yapp.artie.global.common.exception.ErrorResponse;
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
import org.apache.http.HttpEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/artwork")
@RestController
@RequiredArgsConstructor
public class ArtworkController {

  private final ArtworkService artworkService;

  @Operation(summary = "전시 작품 등록", description = "작품(이미지)를 전시에 추가(등록)")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "전시 작품이 성공적으로 추가됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateArtworkResponse.class))),
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
  public ResponseEntity<CreateArtworkResponse> createArtwork(Authentication authentication,
      @RequestBody @Valid
      CreateArtworkRequest createArtworkRequest) {
    Long userId = Long.parseLong(authentication.getName());
    Long id = artworkService.create(createArtworkRequest, userId);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new CreateArtworkResponse(id));
  }

  @Operation(summary = "전시의 작품 목록 조회", description = "전시 상세 페이지의 작품 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "전시 작품 목록 조회",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtworkThumbnailResponse.Page.class))),
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
  public ResponseEntity<Page<ArtworkThumbnailResponse>> getArtworkThumbnails(
      Authentication authentication,
      @Parameter(name = "id", description = "전시 ID", in = ParameterIn.PATH) @Valid @PathVariable("id") Long exhibitId,
      @Parameter(name = "size", description = "페이지네이션의 페이지당 데이터 수", in = ParameterIn.QUERY)
      @RequestParam(value = "size", required = false, defaultValue = "20") int size,
      @Parameter(name = "page", description = "페이지네이션의 페이지 넘버. 0부터 시작함", in = ParameterIn.QUERY)
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @Parameter(name = "direction", description = "페이지네이션의 정렬기준. DESC=최신순, ASC=오래된순", in = ParameterIn.QUERY)
      @RequestParam(name = "direction", required = false, defaultValue = "DESC") Direction direction) {
    Long userId = Long.parseLong(authentication.getName());
    return ResponseEntity.ok()
        .body(artworkService.getArtworkThumbnails(exhibitId, userId, page, size, direction));
  }

  @Operation(summary = "작품 상세 정보 조회", description = "작품 상세 페이지의 작품 상세 정보 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "작품 상세 정보 조회",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ArtworkDetailResponse.class))),
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
  public ResponseEntity<ArtworkDetailResponse> getArtworkDetail(
      Authentication authentication,
      @Parameter(name = "id", description = "작품 ID", in = ParameterIn.PATH) @Valid @PathVariable("id") Long artworkId) {
    Long userId = Long.parseLong(authentication.getName());
    return ResponseEntity.ok().body(artworkService.getArtworkDetail(artworkId, userId));
  }

  @Operation(summary = "작품 탐색 썸네일 목록 조회", description = "작품 상세 페이지의 작품 썸네일 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "작품 썸네일 목록 조회",
          content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ArtworkImageThumbnailResponse.class)))),
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
  public ResponseEntity<List<ArtworkImageThumbnailResponse>> getArtworkImageThumbnails(
      Authentication authentication,
      @Parameter(name = "id", description = "전시 ID", in = ParameterIn.PATH) @Valid @PathVariable("id") Long exhibitId) {
    Long userId = Long.parseLong(authentication.getName());
    return ResponseEntity.ok().body(artworkService.getArtworkImageThumbnails(exhibitId, userId));
  }

  @Operation(summary = "작품 삭제")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "작품이 성공적으로 삭제됨",
          content = @Content(schema = @Schema(implementation = Void.class))),
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
          description = "접근 불가능한 작품",
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
  @DeleteMapping("/{id}")
  public ResponseEntity<? extends HttpEntity> deleteArtwork(
      Authentication authentication,
      @Parameter(name = "id", description = "전시 ID", in = ParameterIn.PATH) @Valid @PathVariable("id") Long id) {
    Long userId = Long.parseLong(authentication.getName());
    artworkService.delete(id, userId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "전시 작품 다중 추가", description = "다중 작품(이미지)를 전시에 추가")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "전시 작품이 성공적으로 추가됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateArtworkBatchResponse.class))),
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
  @PostMapping("batch/{id}")
  public ResponseEntity<CreateArtworkBatchResponse> createArtworkBatch(
      Authentication authentication,
      @Parameter(name = "id", description = "전시 ID", in = ParameterIn.PATH) @Valid @PathVariable("id") Long exhibitId,
      @RequestBody @Valid CreateArtworkBatchRequest createArtworkBatchRequest) {
    Long userId = Long.parseLong(authentication.getName());

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new CreateArtworkBatchResponse(
            artworkService.createBatch(createArtworkBatchRequest.getImageUriList(),
                exhibitId, userId)));
  }

  @Operation(summary = "작품 정보 수정", description = "작품의 작가정보, 작품명, 태그 정보 수정")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "전시 작품이 성공적으로 수정됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
      @ApiResponse(
          responseCode = "400",
          description = "잘못된 입력",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "401",
          description = "인증 오류",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "404",
          description = "찾을 수 없는 회원",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "404",
          description = "찾을 수 없는 전시",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "404",
          description = "찾을 수 없는 태그. 요청한 태그 ID에 해당 하는 태그를 찾을 수 없습니다.",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "409",
          description = "이미 존재하는 태그. 이미 존재하는 태그를 등록하는 경우는 태그 ID를 함께 요청해야합니다.",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
  })
  @PatchMapping("/{id}")
  public ResponseEntity<? extends HttpEntity> updateArtwork(Authentication authentication,
      @Parameter(name = "id", description = "작품 ID", in = ParameterIn.PATH) @Valid @PathVariable("id") Long artworkId,
      @RequestBody @Valid
      UpdateArtworkRequest updateArtworkRequest) {
    Long userId = Long.parseLong(authentication.getName());
    artworkService.update(artworkId, userId, updateArtworkRequest);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "대표 작품 설정", description = "해당 작품을 해당 전시의 대표 작품으로 설정. 기존 작품은 대표 작품에서 해제됨")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "성공적으로 대표 작품으로 설정됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
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
          description = "접근할 수 없는 작품입니다.",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "404",
          description = "찾을 수 없는 회원",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "404",
          description = "찾을 수 없는 작품",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PatchMapping("/main/{id}")
  public ResponseEntity<? extends HttpEntity> setMainArtwork(Authentication authentication,
      @Parameter(name = "id", description = "작품 ID", in = ParameterIn.PATH) @Valid @PathVariable("id") Long artworkId) {
    Long userId = Long.parseLong(authentication.getName());
    artworkService.setMainArtwork(artworkId, userId);
    return ResponseEntity.noContent().build();
  }
}
