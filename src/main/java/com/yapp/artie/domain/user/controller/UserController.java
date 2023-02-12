package com.yapp.artie.domain.user.controller;

import com.google.firebase.auth.FirebaseToken;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.dto.response.CreateUserResponseDto;
import com.yapp.artie.domain.user.dto.response.UserThumbnailResponseDto;
import com.yapp.artie.domain.user.service.UserService;
import com.yapp.artie.domain.user.service.UserThumbnailService;
import com.yapp.artie.global.authentication.JwtService;
import com.yapp.artie.global.exception.common.InvalidValueException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
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

  private void validateUidWithToken(String uid, FirebaseToken decodedToken) {
    if (!decodedToken.getUid().equals(uid)) {
      throw new InvalidValueException();
    }
  }
}
