package com.yapp.artie.user.application.port.in.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Schema(description = "테스트 유저 토큰 발급 Response")
@RequiredArgsConstructor
public class GetTestUserTokenResponse {

  @Schema(description = "테스트 유저 토큰")
  private final String token;
}