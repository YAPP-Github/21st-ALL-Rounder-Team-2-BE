package com.yapp.artie.domain.user.adapter.in.web;

import com.yapp.artie.domain.user.application.port.in.GetUserQuery;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.global.common.annotation.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@WebAdapter
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class GetUserController {

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
    Long userId = getUserId(authentication);
    User user = getUserQuery.loadUserById(userId);

    return ResponseEntity.ok().body(user);
  }

  // TODO : 앱 배포했을 때에는 1L 대신에 exception을 던지도록 변경해야 합니다.
  private Long getUserId(Authentication authentication) {
    if (Optional.ofNullable(authentication).isPresent()) {
      return Long.parseLong(authentication.getName());
    }
    return 1L;
  }
}
