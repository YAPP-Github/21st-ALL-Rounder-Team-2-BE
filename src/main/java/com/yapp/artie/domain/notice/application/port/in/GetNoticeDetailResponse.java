package com.yapp.artie.domain.notice.application.port.in;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Schema(description = "공지사항 상세 Response")
@AllArgsConstructor
public class GetNoticeDetailResponse {

  @Schema(description = "공지사항 아이디")
  private final Long id;

  @Schema(description = "공지일")
  private final LocalDateTime date;

  @Schema(description = "공지사항 제목")
  private final String title;

  @Schema(description = "공지 내용")
  private String contents;
}


