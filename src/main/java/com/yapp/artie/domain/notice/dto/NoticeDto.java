package com.yapp.artie.domain.notice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "공지사항 Response")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeDto {

  @Schema(description = "공지사항 아이디")
  private Long id;

  @Schema(description = "공지일")
  private LocalDateTime date;

  @Schema(description = "공지사항 제목")
  private String title;

  public NoticeDto(Long id, LocalDateTime date, String title) {
    this.id = id;
    this.date = date;
    this.title = title;
  }
}
