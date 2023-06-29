package com.yapp.artie.user.adapter.in.web;

import com.yapp.artie.global.common.annotation.WebAdapter;
import com.yapp.artie.user.application.port.in.query.GetUserQuery;
import com.yapp.artie.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@WebAdapter
@RequestMapping("/user")
@RequiredArgsConstructor
class GetUserController {

  private final GetUserQuery getUserQuery;

  @Operation(summary = "유저 조회", description = "토큰 기반 유저 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "유저가 성공적으로 조회됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
  })
  @GetMapping("/me")
  public ResponseEntity<User> me(Authentication authentication) {
    Long userId = Long.parseLong(authentication.getName());
    User user = getUserQuery.loadUserById(userId);

    return ResponseEntity.ok().body(user);
  }
}
