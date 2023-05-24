package com.yapp.artie.domain.user.adapter.in.web;

import com.yapp.artie.domain.user.application.port.in.GetUserJoinDateQuery;
import com.yapp.artie.domain.user.application.port.in.GetUserJoinDateResponse;
import com.yapp.artie.global.common.annotation.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@WebAdapter
@RequestMapping("/user")
@RequiredArgsConstructor
class GetUserJoinDateController {

  private final GetUserJoinDateQuery getUserJoinDateQuery;

  @Operation(summary = "유저 가입일 조회", description = "유저 가입일(createdAt) 조회")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "유저 가입일을 성공적으로 조회함")})
  @GetMapping("/join")
  public ResponseEntity<GetUserJoinDateResponse> getUserJoinDate(Authentication authentication) {
    Long userId = Long.parseLong(authentication.getName());
    LocalDateTime joinDate = getUserJoinDateQuery.loadUserJoinDate(userId);

    return ResponseEntity.ok().body(new GetUserJoinDateResponse(joinDate));
  }
}
