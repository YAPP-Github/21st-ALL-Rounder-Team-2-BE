package com.yapp.artie.domain.user.controller;

import com.google.firebase.auth.FirebaseToken;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.dto.response.CreateUserResponseDto;
import com.yapp.artie.domain.user.dto.response.UserThumbnailResponseDto;
import com.yapp.artie.domain.user.service.UserService;
import com.yapp.artie.domain.user.service.UserThumbnailService;
import com.yapp.artie.global.authentication.JwtService;
import com.yapp.artie.global.exception.common.InvalidValueException;
import com.yapp.artie.global.exception.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserThumbnailService userThumbnailService;
  private final JwtService jwtService;

  @Operation(summary = "유저 생성", description = "Firebase를 통해 생성한 UID 기반 유저 생성")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "유저가 성공적으로 생성됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserResponseDto.class))),
  })
  @PostMapping()
  public ResponseEntity<CreateUserResponseDto> register(
      HttpServletRequest request, @RequestParam("uid") String uid) {
    String authorization = request.getHeader("Authorization");
    FirebaseToken decodedToken = jwtService.verify(authorization);
    validateUidWithToken(uid, decodedToken);

    return ResponseEntity.status(HttpStatus.CREATED).body(
        userService.register(decodedToken.getUid(), decodedToken.getName(),
            decodedToken.getPicture()));
  }

  //TODO : 인가테스트 용, 삭제 필요
  @Operation(summary = "유저 조회", description = "토큰 기반 유저 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "유저가 성공적으로 조회됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
  })
  @GetMapping("/me")
  public ResponseEntity<User> me(Authentication authentication) {
    // Long userId = Long.parseLong(authentication.getName());
    Long userId = 1L;
    User user = userService.findById(userId);

    return ResponseEntity.ok().body(user);
  }

  @Operation(summary = "유저 삭제", description = "회원 탈퇴")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "유저가 성공적으로 삭제됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseEntity.class))),
  })
  @DeleteMapping("/")
  public ResponseEntity<? extends HttpEntity> deleteUser(Authentication authentication) {
    // Long userId = Long.parseLong(authentication.getName());
    Long userId = 1L;
    userService.delete(userId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "마이페이지 썸네일 조회", description = "사용자 닉네임, 전시 개수 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "썸네일이 성공적으로 조회됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
  })
  @GetMapping("/my-page")
  public ResponseEntity<UserThumbnailResponseDto> my(Authentication authentication) {
    // Long userId = Long.parseLong(authentication.getName());
    Long userId = 1L;
    return ResponseEntity.ok().body(userThumbnailService.getUserThumbnail(userId));
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
    userService.updateUserName(userId, name);
    return ResponseEntity.noContent().build();
  }

  private void validateUidWithToken(String uid, FirebaseToken decodedToken) {
    if (!decodedToken.getUid().equals(uid)) {
      throw new InvalidValueException();
    }
  }

  // TODO : 앱 배포했을 때에는 0L 대신에 exception을 던지도록 변경해야 합니다.
  private Long getUserId(Authentication authentication) {
    if (Optional.ofNullable(authentication).isPresent()) {
      return Long.parseLong(authentication.getName());
    }
    return 1L;
  }
}
