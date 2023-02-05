package com.yapp.artie.domain.archive.controller;


import com.yapp.artie.domain.archive.dto.exhibit.CalendarExhibitRequestDto;
import com.yapp.artie.domain.archive.dto.exhibit.CalendarExhibitResponseDto;
import com.yapp.artie.domain.archive.dto.exhibit.CreateExhibitRequestDto;
import com.yapp.artie.domain.archive.dto.exhibit.CreateExhibitResponseDto;
import com.yapp.artie.domain.archive.dto.exhibit.PostDetailInfo;
import com.yapp.artie.domain.archive.dto.exhibit.PostInfoDto;
import com.yapp.artie.domain.archive.dto.exhibit.UpdateExhibitRequestDto;
import com.yapp.artie.domain.archive.service.ExhibitService;
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
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
          description = "전시가 성공적으로 조회됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostInfoDto.class))),
  })
  @GetMapping("/{id}")
  public ResponseEntity<PostInfoDto> getPost(Authentication authentication,
      @PathVariable("id") Long id) {
    Long userId = getUserId(authentication);
    PostInfoDto exhibitInformation = exhibitService.getExhibitInformation(id, userId);

    return ResponseEntity.ok().body(exhibitInformation);
  }

  @Operation(summary = "월별 전시 조회", description = "월별로 전시 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "월별 전시가 성공적으로 조회됨",
          content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CalendarExhibitResponseDto.class)))),
  })
  @GetMapping("/monthly")
  public ResponseEntity<List<CalendarExhibitResponseDto>> getPostsByMonthly(
      Authentication authentication,
      @Parameter(example = "2023", description = "yyyy")
      @RequestParam("year") int year,
      @Parameter(example = "02", description = "mm")
      @RequestParam("month") int month) {
    Long userId = getUserId(authentication);

    return ResponseEntity.ok()
        .body(
            exhibitService.getExhibitByMonthly(new CalendarExhibitRequestDto(year, month), userId));
  }

  @Operation(summary = "임시 저장 전시 조회", description = "임시 저장된 전시 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "임시 저장된 전시 목록이 성공적으로 조회됨",
          content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PostInfoDto.class)))),
  })
  @GetMapping("/draft")
  public ResponseEntity<List<PostInfoDto>> getDraftPosts(Authentication authentication) {
    Long userId = getUserId(authentication);
    return ResponseEntity.ok().body(exhibitService.getDraftExhibits(userId));
  }

  @Operation(summary = "홈 화면 전시 조회(특정 카테고리)", description =
      "저장된 전시 중 페이지네이션을 이용해 값을 가져온다. 이곳의 id는 category id를 의미하며 "
          + "size의 기본값은 20이다. sort는 기본값이 최신 순이고, ?sort=contents.date,ASC 는 오래된 순이다. "
          + "오래된 순의 예시처럼 콤마를 기준으로 [<정렬 컬럼>,<정렬 타입 형식>]으로 쿼리 파라미터를 전달해야 한다."
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "홈 화면 전시 목록(특정 카테고리)이 성공적으로 조회됨",
          content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PostDetailInfo.class)))),
  })
  @GetMapping("/home/{id}")
  public ResponseEntity<Page<PostDetailInfo>> getPostPage(
      Authentication authentication,
      @PageableDefault(
          size = 20, sort = {"contents.date"}, direction = Sort.Direction.DESC
      )
      Pageable pageable,
      @PathVariable("id") Long id) {
    Long userId = getUserId(authentication);
    Page<PostDetailInfo> pageResult = exhibitService.getExhibitByPage(id, userId, pageable);

    return ResponseEntity.ok().body(pageResult);
  }

  @Operation(summary = "홈 화면 전시 조회(전체 기록)", description = "용례는 홈 화면 전시 조회(특정 카테고리)와 같다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "홈 화면 전시 목록(전체 기록)이 성공적으로 조회됨",
          content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PostDetailInfo.class)))),
  })
  @GetMapping("/home")
  public ResponseEntity<Page<PostDetailInfo>> getAllPostPage(
      Authentication authentication,
      @PageableDefault(
          size = 20, sort = {"contents.date"}, direction = Sort.Direction.DESC
      )
      Pageable pageable) {
    Long userId = getUserId(authentication);
    Page<PostDetailInfo> pageResult = exhibitService.getAllExhibitByPage(userId, pageable);

    return ResponseEntity.ok().body(pageResult);
  }

  @Operation(summary = "전시 생성", description = "처음 전시 정보 등록시 임시 전시로 생성됨")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "전시가 성공적으로 생성됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateExhibitResponseDto.class))),
  })
  @PostMapping()
  public ResponseEntity<CreateExhibitResponseDto> createPost(Authentication authentication,
      @RequestBody CreateExhibitRequestDto createExhibitRequestDto) {
    Long userId = getUserId(authentication);
    Long id = exhibitService.create(createExhibitRequestDto, userId);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new CreateExhibitResponseDto(id));
  }

  @Operation(summary = "전시 수정", description = "전시 수정")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "전시가 성공적으로 수정됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))),
  })
  @PutMapping("/{id}")
  public ResponseEntity<? extends HttpEntity> updatePost(Authentication authentication,
      @PathVariable("id") Long id, @RequestBody
  UpdateExhibitRequestDto updateExhibitRequestDto) {
    Long userId = getUserId(authentication);

    exhibitService.update(updateExhibitRequestDto, id, userId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "전시 발행", description = "임시 저장 전시를 영구 저장")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "전시가 성공적으로 발행됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))),
  })
  @PutMapping("/publish/{id}")
  public ResponseEntity<? extends HttpEntity> publishPost(Authentication authentication,
      @PathVariable("id") Long id) {
    Long userId = getUserId(authentication);
    exhibitService.publish(id, userId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "전시 상세 정보 조회", description = "전시 상세 페이지 내 전시 상세 정보 조회. 카테고리 정보, 대표이미지 정보를 포함함.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "전시 상세 정보 반환",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostDetailInfo.class))),
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
  @GetMapping("/detail/{id}")
  public ResponseEntity<PostDetailInfo> getPostInfoWithCategory(
      Authentication authentication,
      @Parameter(name = "id", description = "전시 ID", in = ParameterIn.PATH) @Valid @PathVariable("id") Long id) {
    Long userId = getUserId(authentication);
    return ResponseEntity.ok().body(exhibitService.getDetailExhibitInformation(id, userId));
  }

  // TODO : 앱 배포했을 때에는 1L 대신에 exception을 던지도록 변경해야 합니다.
  private Long getUserId(Authentication authentication) {
    if (Optional.ofNullable(authentication).isPresent()) {
      return Long.parseLong(authentication.getName());
    }
    return 1L;
  }
}
