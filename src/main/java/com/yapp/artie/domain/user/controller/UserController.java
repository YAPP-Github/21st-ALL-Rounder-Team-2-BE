package com.yapp.artie.domain.user.controller;

import com.google.firebase.auth.FirebaseToken;
import com.yapp.artie.domain.user.dto.response.CreateUserResponseDto;
import com.yapp.artie.domain.user.service.UserService;
import com.yapp.artie.global.authentication.JwtService;
import com.yapp.artie.global.error.exception.BusinessException;
import com.yapp.artie.global.error.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final JwtService jwtService;


  @Operation(summary = "유저 생성", description = "Firebase를 통해 생성한 UID 기반 유저 생성")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "유저가 성공적으로 생성됨", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserResponseDto.class))),
  })
  @PostMapping()
  public ResponseEntity<CreateUserResponseDto> register(
      @RequestHeader("Authorization") String authorization,
      @RequestParam("uid") String uid
  ) {
    FirebaseToken decodedToken = jwtService.verify(authorization);
    validateUidWithToken(uid, decodedToken);
    validateDuplicateUser(uid);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(userService.register(decodedToken.getUid(),
            decodedToken.getName()));
  }

  @GetMapping("/me")
  public ResponseEntity<String> me(Authentication authentication) {
    return ResponseEntity.ok().body(authentication.getName());
  }

  private void validateUidWithToken(String uid, FirebaseToken decodedToken) {
    if (!decodedToken.getUid().equals(uid)) {
      throw new InvalidUidException();
    }
  }

  private void validateDuplicateUser(String uid) {
    userService.findByUid(uid)
        .ifPresent(existedUser -> {
          throw new UserExistException();
        });
  }

  static class InvalidUidException extends BusinessException {

    public InvalidUidException() {
      super(ErrorCode.AUTH_INVALID_USERINFO);
    }
  }

  static class UserExistException extends BusinessException {

    public UserExistException() {
      super(ErrorCode.USER_ALREADY_EXISTS);
    }
  }
}
