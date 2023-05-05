package com.yapp.artie.domain.user.application.port.in;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Schema(description = "유저 가입일 조회 Response")
@RequiredArgsConstructor
public class GetUserJoinDateResponse {

  @Schema(description = "유저 가입일")
  private final LocalDateTime joinDate;
}