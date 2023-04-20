package com.yapp.artie.domain.notice.adapter.in.web;


import com.yapp.artie.domain.notice.application.port.in.GetNoticeDetailQuery;
import com.yapp.artie.domain.notice.application.port.in.GetNoticeDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/notice")
@RequiredArgsConstructor
@RestController
public class GetNoticeDetailController {

  private final GetNoticeDetailQuery getNoticeDetailQuery;

  @Operation(summary = "공지사항 상세 조회", description = "특정 공지사항 상세를 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "공지사항 상세가 성공적으로 조회됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = GetNoticeDetailResponse.class))),
  })
  @GetMapping("/{id}")
  public ResponseEntity<GetNoticeDetailResponse> getNoticeDetail(@PathVariable("id") Long id) {
    return ResponseEntity.ok(getNoticeDetailQuery.loadNoticeDetail(id));
  }
}
