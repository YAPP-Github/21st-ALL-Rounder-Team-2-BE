package com.yapp.artie.domain.exhibition.domain.controller;


import com.yapp.artie.domain.exhibition.domain.dto.exhibition.CreateExhibitionRequest;
import com.yapp.artie.domain.exhibition.domain.dto.exhibition.CreateExhibitionResponse;
import com.yapp.artie.domain.exhibition.domain.dto.exhibition.ExhibitionByCategoryResponse;
import com.yapp.artie.domain.exhibition.domain.dto.exhibition.ExhibitionByDateResponse;
import com.yapp.artie.domain.exhibition.domain.dto.exhibition.ExhibitionByMonthlyResponse;
import com.yapp.artie.domain.exhibition.domain.dto.exhibition.ExhibitionDetailResponse;
import com.yapp.artie.domain.exhibition.domain.dto.exhibition.ExhibitionDraftResponse;
import com.yapp.artie.domain.exhibition.domain.dto.exhibition.UpdateExhibitionRequest;
import com.yapp.artie.domain.exhibition.domain.service.ExhibitionService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/post")
@RestController
@RequiredArgsConstructor
public class ExhibitionController {

  private final ExhibitionService exhibitionService;

  @Operation(summary = "전시 상세 정보 조회", description = "전시 상세 페이지 내 전시 상세 정보 조회. 카테고리 정보, 대표이미지 정보를 포함함.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "전시 상세 정보 반환",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExhibitionDetailResponse.class))),
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
  @GetMapping("/{id}")
  public ResponseEntity<ExhibitionDetailResponse> getExhibition(
      Authentication authentication,
      @Parameter(name = "id", description = "전시 ID", in = ParameterIn.PATH) @Valid @PathVariable("id") Long id) {
    Long userId = Long.parseLong(authentication.getName());
    return ResponseEntity.ok().body(exhibitionService.getExhibition(id, userId));
  }

  @Operation(summary = "월별 전시 조회", description = "월별로 전시 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "월별 전시가 성공적으로 조회됨",
          content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ExhibitionByMonthlyResponse.class)))),
  })
  @GetMapping("/monthly")
  public ResponseEntity<List<ExhibitionByMonthlyResponse>> getExhibitionsByMonthly(
      Authentication authentication,
      @Parameter(example = "2023", description = "yyyy")
      @RequestParam("year") int year,
      @Parameter(example = "02", description = "mm")
      @RequestParam("month") int month) {
    Long userId = Long.parseLong(authentication.getName());

    return ResponseEntity.ok()
        .body(
            exhibitionService.getExhibitionsByMonthly(year, month, userId));
  }

  @Operation(summary = "임시 저장 전시 조회", description = "임시 저장된 전시 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "임시 저장된 전시 목록이 성공적으로 조회됨",
          content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ExhibitionDraftResponse.class)))),
  })
  @GetMapping("/draft")
  public ResponseEntity<List<ExhibitionDraftResponse>> getDraftExhibitions(
      Authentication authentication) {
    Long userId = Long.parseLong(authentication.getName());
    return ResponseEntity.ok().body(exhibitionService.getDraftExhibitions(userId));
  }

  @Deprecated
  @Operation(summary = "홈 화면 전시 조회(특정 카테고리)", description =
      "해당 API는 [GET] /post/home API와 통합되어, deprecated될 예정입니다. "
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "홈 화면 전시 목록(특정 카테고리)이 성공적으로 조회됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExhibitionDetailResponse.HomePage.class))),
  })
  @GetMapping("/home/{id}")
  public ResponseEntity<Page<ExhibitionDetailResponse>> getExhibitionsByCategoryForHomePage(
      Authentication authentication,
      @Parameter(name = "size", description = "페이지네이션의 페이지당 데이터 수", in = ParameterIn.QUERY)
      @RequestParam(value = "size", required = false, defaultValue = "20") int size,
      @Parameter(name = "page", description = "페이지네이션의 페이지 넘버. 0부터 시작함", in = ParameterIn.QUERY)
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @Parameter(name = "direction", description = "페이지네이션의 정렬기준. DESC=최신순, ASC=오래된순", in = ParameterIn.QUERY)
      @RequestParam(name = "direction", required = false, defaultValue = "DESC") Direction direction,
      @PathVariable("id") Long id) {

    Long userId = Long.parseLong(authentication.getName());
    Page<ExhibitionDetailResponse> pageResult = exhibitionService.getExhibitionsForHomePage(id,
        userId,
        page, size, direction);

    return ResponseEntity.ok().body(pageResult);
  }

  @Operation(summary = "홈 화면 전시 조회(전체 기록/특정 카테고리)", description = "홈 화면의 전체 기록 혹은 특정 카테고리 별, 상단 고정 설정을 반영한 전시 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "홈 화면 전시 목록이 성공적으로 조회됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExhibitionDetailResponse.HomePage.class))),
  })
  @GetMapping("/home")
  public ResponseEntity<Page<ExhibitionDetailResponse>> getExhibitionsForHomePage(
      Authentication authentication,
      @Parameter(name = "size", description = "페이지네이션의 페이지당 데이터 수", in = ParameterIn.QUERY)
      @RequestParam(value = "size", required = false, defaultValue = "20") int size,
      @Parameter(name = "page", description = "페이지네이션의 페이지 넘버. 0부터 시작함", in = ParameterIn.QUERY)
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @Parameter(name = "direction", description = "페이지네이션의 정렬기준. DESC=최신순, ASC=오래된순", in = ParameterIn.QUERY)
      @RequestParam(name = "direction", required = false, defaultValue = "DESC") Direction direction,
      @Parameter(name = "category", description = "카테고리 ID. 홈 화면의 카테고리별 전시 목록 조회시 해당 파라미터를 입력해야함.", in = ParameterIn.QUERY)
      @RequestParam(name = "category", required = false) Long categoryId
  ) {

    Long userId = Long.parseLong(authentication.getName());
    Page<ExhibitionDetailResponse> pageResult = exhibitionService.getExhibitionsForHomePage(
        categoryId,
        userId, page, size, direction);

    return ResponseEntity.ok().body(pageResult);
  }

  @Operation(summary = "전시 생성", description = "처음 전시 정보 등록시 임시 전시로 생성됨")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "전시가 성공적으로 생성됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateExhibitionResponse.class))),
  })
  @PostMapping()
  public ResponseEntity<CreateExhibitionResponse> createExhibition(Authentication authentication,
      @RequestBody CreateExhibitionRequest createExhibitionRequest) {
    Long userId = Long.parseLong(authentication.getName());
    Long id = exhibitionService.create(createExhibitionRequest, userId);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new CreateExhibitionResponse(id));
  }

  @Operation(summary = "전시 수정", description = "전시 수정")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "전시가 성공적으로 수정됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))),
  })
  @PatchMapping("/{id}")
  public ResponseEntity<? extends HttpEntity> updateExhibition(Authentication authentication,
      @PathVariable("id") Long id, @RequestBody UpdateExhibitionRequest updateExhibitRequest) {
    Long userId = Long.parseLong(authentication.getName());

    exhibitionService.update(updateExhibitRequest, id, userId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "전시 삭제", description = "사용자 전시 삭제")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "전시가 성공적으로 삭제됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))),
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<? extends HttpEntity> deleteExhibition(Authentication authentication,
      @PathVariable("id") Long id) {
    Long userId = Long.parseLong(authentication.getName());
    exhibitionService.delete(id, userId);

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
  public ResponseEntity<? extends HttpEntity> publishExhibition(Authentication authentication,
      @PathVariable("id") Long id) {
    Long userId = Long.parseLong(authentication.getName());
    exhibitionService.publish(id, userId);
    return ResponseEntity.noContent().build();
  }

  @Deprecated
  @Operation(summary = "deprecated : 전시 상세 정보 조회", description = "해당 API는 deprecated 되었으니, [GET] /post/{id}를 사용해주세요.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "전시 상세 정보 반환",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExhibitionDetailResponse.class))),
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
  public ResponseEntity<ExhibitionDetailResponse> getExhibitionInfoWithCategory(
      Authentication authentication,
      @Parameter(name = "id", description = "전시 ID", in = ParameterIn.PATH) @Valid @PathVariable("id") Long id) {
    Long userId = Long.parseLong(authentication.getName());
    return ResponseEntity.ok().body(exhibitionService.getExhibition(id, userId));
  }

  @Operation(summary = "전시 상단 고정 설정", description = "특정 전시를 홈페이지에서 전체 기록 혹은 특정 카테고리에 대해 상단에 고정되어 조회할 수 있도록 설정")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "성공적으로 상단 고정 설정됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
  })
  @PatchMapping("/pin")
  public ResponseEntity<? extends HttpEntity> updateExhibitionPinType(
      Authentication authentication,
      @Parameter(name = "id", description = "전시 ID", in = ParameterIn.QUERY)
      @RequestParam(value = "id", required = true) Long exhibitId,
      @Parameter(name = "category", description = "카테고리에 고정하는지의 여부. "
          + "true일 경우, 해당 전시의 카테고리 상단 고정으로, false일 경우 전체 기록의 상단 고정 설정으로 처리",
          in = ParameterIn.QUERY)
      @RequestParam(value = "category", required = true, defaultValue = "true") boolean categoryType,
      @Parameter(name = "pinned", description = "고정 여부. 고정하도록 설정한다면 true, 고정 해제하도록 설정한다면 false", in = ParameterIn.QUERY)
      @RequestParam(value = "pinned", required = true, defaultValue = "true") boolean pinned
  ) {
    Long userId = Long.parseLong(authentication.getName());
    exhibitionService.updateExhibitionPinType(userId, exhibitId, categoryType, pinned);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "카테고리별 전시 목록 조회(카테고리 페이지)", description = "카테고리 페이지에서 카테고리별 전시 목록을 조회할 때, 상단 고정 설정이 반영되지 않은 전시 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "카테고리별 전시 목록 반환", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExhibitionByCategoryResponse.Page.class)))
  })
  @GetMapping("/category/{id}")
  public ResponseEntity<Page<ExhibitionByCategoryResponse>> getExhibitionsByCategory(
      Authentication authentication,
      @Parameter(name = "page", description = "페이지네이션의 페이지 넘버. 0부터 시작함", in = ParameterIn.QUERY)
      @RequestParam(value = "page", required = false, defaultValue = "0") int page,
      @Parameter(name = "size", description = "페이지네이션의 페이지당 데이터 수", in = ParameterIn.QUERY)
      @RequestParam(value = "size", required = false, defaultValue = "20") int size,
      @Parameter(name = "id", description = "카테고리 ID", in = ParameterIn.PATH) @Valid @PathVariable("id") Long categoryId
  ) {
    Long userId = Long.parseLong(authentication.getName());
    return ResponseEntity.ok(
        exhibitionService.getExhibitionsByCategory(userId, categoryId, page, size));
  }

  @Operation(summary = "특정 날짜의 전시 정보 목록 조회(캘린더 페이지)", description = "캘린더 페이지에서 특정 날짜를 클릭했을 때, 해당 날짜의 전시가 여러개일 경우, 전시 목록을 조회할 수 있도록 정보를 반환하는 API")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "특정 날짜의 전시 정보 목록 반환",
          content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ExhibitionByDateResponse.class)))
      )
  })
  @GetMapping("/date")
  public ResponseEntity<List<ExhibitionByDateResponse>> getExhibitionsByDate(
      Authentication authentication,
      @Parameter(name = "year", description = "조회할 연도", in = ParameterIn.QUERY, example = "2023")
      @RequestParam(value = "year", required = true) int year,
      @Parameter(name = "month", description = "조회할 달", in = ParameterIn.QUERY, example = "1")
      @RequestParam(value = "month", required = true) int month,
      @Parameter(name = "day", description = "조회할 일", in = ParameterIn.QUERY, example = "1")
      @RequestParam(value = "day", required = true) int day
  ) {
    Long userId = Long.parseLong(authentication.getName());
    return ResponseEntity.ok(
        exhibitionService.getExhibitionsByDate(userId, year, month, day));
  }
}
