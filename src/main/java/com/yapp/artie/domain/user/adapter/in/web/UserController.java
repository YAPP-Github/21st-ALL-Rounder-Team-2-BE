package com.yapp.artie.domain.user.adapter.in.web;

import com.yapp.artie.domain.user.adapter.out.persistence.UserJpaEntity;
import com.yapp.artie.domain.user.application.port.in.GetUserQuery;
import com.yapp.artie.domain.user.application.port.in.GetUserThumbnailQuery;
import com.yapp.artie.domain.user.application.port.in.RenameUserUseCase;
import com.yapp.artie.domain.user.application.port.in.UserWithdrawalUseCase;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.dto.response.UserThumbnailResponseDto;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/user")
@WebAdapter
@RequiredArgsConstructor
public class UserController {

  private final UserWithdrawalUseCase userWithdrawalUseCase;
  private final RenameUserUseCase renameUserUseCase;
  private final GetUserThumbnailQuery getUserThumbnailQuery;
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

  @Operation(summary = "유저 삭제", description = "회원 탈퇴")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "유저가 성공적으로 삭제됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))),
  })
  @DeleteMapping()
  public ResponseEntity<? extends HttpEntity> deleteUser(Authentication authentication) {

    Long userId = getUserId(authentication);
    userWithdrawalUseCase.delete(userId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "마이페이지 썸네일 조회", description = "사용자 닉네임, 전시 개수 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "썸네일이 성공적으로 조회됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserJpaEntity.class))),
  })
  @GetMapping("/my-page")
  public ResponseEntity<UserThumbnailResponseDto> my(Authentication authentication) {

    Long userId = getUserId(authentication);
    return ResponseEntity.ok().body(getUserThumbnailQuery.loadUserThumbnailById(userId));
  }

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
  @PatchMapping("/{id}")
  public ResponseEntity<? extends HttpEntity> updateUserName(Authentication authentication,
      @Parameter(name = "id", description = "유저 ID", in = ParameterIn.PATH) @Valid @PathVariable("id") Long artworkId,
      @Parameter(name = "name", description = "변경할 닉네임", in = ParameterIn.QUERY) @Valid @RequestParam("name") String name) {

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
