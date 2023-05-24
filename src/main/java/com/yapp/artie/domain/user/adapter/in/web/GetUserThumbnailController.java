package com.yapp.artie.domain.user.adapter.in.web;

import com.yapp.artie.domain.user.adapter.out.persistence.UserJpaEntity;
import com.yapp.artie.domain.user.application.port.in.GetUserThumbnailQuery;
import com.yapp.artie.domain.user.application.port.in.GetUserThumbnailResponse;
import com.yapp.artie.global.common.annotation.WebAdapter;
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
class GetUserThumbnailController {

  private final GetUserThumbnailQuery getUserThumbnailQuery;

  @Operation(summary = "마이페이지 썸네일 조회", description = "사용자 닉네임, 전시 개수 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "썸네일이 성공적으로 조회됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserJpaEntity.class))),
  })
  @GetMapping("/my-page")
  public ResponseEntity<GetUserThumbnailResponse> my(Authentication authentication) {
    Long userId = Long.parseLong(authentication.getName());
    return ResponseEntity.ok().body(getUserThumbnailQuery.loadUserThumbnailById(userId));
  }
}
