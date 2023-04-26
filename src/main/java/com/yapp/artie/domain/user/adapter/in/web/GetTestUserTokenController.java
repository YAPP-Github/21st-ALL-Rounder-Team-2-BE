package com.yapp.artie.domain.user.adapter.in.web;

import com.yapp.artie.domain.user.application.port.in.GetTestUserTokenQuery;
import com.yapp.artie.domain.user.application.port.in.GetTestUserTokenResponse;
import com.yapp.artie.global.common.annotation.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// TODO: 추후 서비스 QA 기간 종료된 이후, 개발 서버에만 공개하거나, 삭제해야함
@WebAdapter
@RequestMapping("/user")
@RequiredArgsConstructor
class GetTestUserTokenController {

  private final GetTestUserTokenQuery getTestUserTokenQuery;

  @Operation(summary = "테스트 유저 토큰 발급 API", description = "테스트 유저에 대한 Firebase Custom Token을 발급하여 반환함")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "테스트 유저에 대한 Firebase Custom Token을 성공적으로 발급하여 반환함")
  })
  @GetMapping("/test/token")
  public ResponseEntity<GetTestUserTokenResponse> getTestUserToken() {
    Long userId = 1L;
    String token = getTestUserTokenQuery.loadTestUserToken(userId);
    return ResponseEntity.ok().body(new GetTestUserTokenResponse(token));
  }
}
