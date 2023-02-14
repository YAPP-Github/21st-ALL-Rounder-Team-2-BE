package com.yapp.artie.domain.notice.controller;


import com.yapp.artie.domain.notice.dto.NoticeDetailInfo;
import com.yapp.artie.domain.notice.service.NoticeService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/notice")
@RequiredArgsConstructor
@RestController
public class NoticeController {

  private final NoticeService noticeService;

  @Operation(summary = "공지사항 목록 조회", description = "공지사항 목록을 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "공지사항 목록이 성공적으로 조회됨",
          content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = NoticeDetailInfo.class)))),
  })
  @GetMapping()
  public ResponseEntity<List<NoticeDetailInfo>> getNotices() {
    return ResponseEntity.ok(noticeService.notices());
  }

  @Operation(summary = "공지사항 상세 조회", description = "특정 공지사항 상세를 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "공지사항 상세가 성공적으로 조회됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = NoticeDetailInfo.class))),
  })
  @GetMapping("/{id}")
  public ResponseEntity<NoticeDetailInfo> getNoticeDetail(@PathVariable("id") Long id) {
    return ResponseEntity.ok(noticeService.notice(id));
  }
}
