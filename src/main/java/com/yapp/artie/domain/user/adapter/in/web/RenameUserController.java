package com.yapp.artie.domain.user.adapter.in.web;

import com.yapp.artie.domain.user.application.port.in.RenameUserUseCase;
import com.yapp.artie.global.annotation.WebAdapter;
import com.yapp.artie.global.exception.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@WebAdapter
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class RenameUserController {

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

    System.out.println(authentication);
    Long userId = getUserId(authentication);
    renameUserUseCase.rename(userId, name);
    return ResponseEntity.noContent().build();
  }

  // TODO : 앱 배포했을 때에는 1L 대신에 exception을 던지도록 변경해야 합니다.
  private Long getUserId(Authentication authentication) {
    if (Optional.ofNullable(authentication).isPresent()) {
      return Long.parseLong(authentication.getName());
    }
    return 1L;
  }
}

