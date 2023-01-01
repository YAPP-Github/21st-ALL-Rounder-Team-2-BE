package com.yapp.artie.domain.user.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.yapp.artie.global.error.exception.ErrorCode;
import com.yapp.artie.global.error.exception.InvalidValueException;
import com.yapp.artie.domain.user.domain.User;
import com.yapp.artie.domain.user.dto.response.CreateUserResponseDto;
import com.yapp.artie.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @Operation(summary = "유저 생성", description = "Firebase를 통해 생성한 UID 기반 유저 생성")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "유저가 성공적으로 생성됨", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserResponseDto.class))),
  })
  @PostMapping("/{uid}")
  public ResponseEntity<CreateUserResponseDto> createUserByUid(HttpServletRequest request,
      @PathVariable(name = "uid") String uid) throws FirebaseAuthException {
    if (!request.getAttribute("uid").equals(uid)) {
      throw new InvalidUidException("uid가 일치하지 않습니다" + uid);
    }

    userService.findByUid(uid)
        .ifPresent(existedUser -> {
          throw new UserExistException("user UID = " + uid);
        });
    User newUser = userService.createUser(uid);
    // Firebase auth 예외 처리 필요

    return ResponseEntity.ok().body(new CreateUserResponseDto(newUser.getId()));
  }


  static class InvalidUidException extends InvalidValueException {

    public InvalidUidException(String value) {
      super(value, ErrorCode.AUTH_INVALID_USERINFO);
    }
  }

  static class UserExistException extends InvalidValueException {

    public UserExistException(String value) {
      super(value, ErrorCode.USER_ALREADY_EXISTS);
    }
  }
}
