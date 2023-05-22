package com.yapp.artie.domain.user.adapter.in.web;

import com.yapp.artie.domain.user.application.port.in.UserWithdrawalUseCase;
import com.yapp.artie.global.common.annotation.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@WebAdapter
@RequestMapping("/user")
@RequiredArgsConstructor
class UserWithdrawalController {

  private final UserWithdrawalUseCase userWithdrawalUseCase;

  @Operation(summary = "유저 삭제", description = "회원 탈퇴")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "유저가 성공적으로 삭제됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))),
  })
  @DeleteMapping()
  public ResponseEntity<? extends HttpEntity> deleteUser(Authentication authentication) {
    Long userId = Long.parseLong(authentication.getName());
    userWithdrawalUseCase.delete(userId);
    return ResponseEntity.noContent().build();
  }
}
