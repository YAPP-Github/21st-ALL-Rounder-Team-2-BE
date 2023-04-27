package com.yapp.artie.domain.notice.adapter.in.web;


import com.yapp.artie.domain.notice.application.port.in.GetNoticeDetailResponse;
import com.yapp.artie.domain.notice.application.port.in.GetNoticeListQuery;
import com.yapp.artie.global.common.annotation.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@WebAdapter
@RequestMapping("/notice")
@RequiredArgsConstructor
class GetNoticeListController {

  private final GetNoticeListQuery getNoticeListQuery;

  @Operation(summary = "공지사항 목록 조회", description = "공지사항 목록을 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "공지사항 목록이 성공적으로 조회됨",
          content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GetNoticeDetailResponse.class)))),
  })
  @GetMapping()
  public ResponseEntity<List<GetNoticeDetailResponse>> getNotices() {
    return ResponseEntity.ok(getNoticeListQuery.loadNoticeList());
  }
}
