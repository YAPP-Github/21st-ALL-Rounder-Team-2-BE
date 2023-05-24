package com.yapp.artie.domain.user.adapter.in.web;

import com.yapp.artie.domain.user.application.port.in.RenameUserUseCase;
import com.yapp.artie.global.common.annotation.WebAdapter;
import com.yapp.artie.global.common.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@WebAdapter
@RequestMapping("/user")
@RequiredArgsConstructor
class RenameUserController {

  private final RenameUserUseCase renameUserUseCase;

  @Operation(summary = "유저 닉네임 수정", description = "유저의 서비스 닉네임 수정")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "닉네임이 성공적으로 수정됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
      @ApiResponse(
          responseCode = "400",
          description = "잘못된 입력",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "401",
          description = "인증 오류",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(
          responseCode = "404",
          description = "찾을 수 없는 회원",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
  })
  @PatchMapping()
  public ResponseEntity<? extends HttpEntity> rename(Authentication authentication,
      @Parameter(name = "name", description = "변경할 닉네임", in = ParameterIn.QUERY) @Valid @RequestParam("name") String name) {
    Long userId = Long.parseLong(authentication.getName());
    renameUserUseCase.rename(userId, name);
    return ResponseEntity.noContent().build();
  }
}

