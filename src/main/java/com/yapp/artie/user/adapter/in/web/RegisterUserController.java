package com.yapp.artie.user.adapter.in.web;


import com.yapp.artie.global.common.annotation.WebAdapter;
import com.yapp.artie.global.common.exception.InvalidValueException;
import com.yapp.artie.user.application.port.in.command.RegisterUserUseCase;
import com.yapp.artie.user.application.port.in.response.RegisterUserResponse;
import com.yapp.artie.user.application.port.out.TokenParsingPort;
import com.yapp.artie.user.domain.ArtieToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@WebAdapter
@RequestMapping("/user")
@RequiredArgsConstructor
class RegisterUserController {

  private final TokenParsingPort tokenParsingPort;
  private final RegisterUserUseCase registerUserUseCase;

  @Operation(summary = "유저 생성", description = "Firebase를 통해 생성한 UID 기반 유저 생성")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "유저가 성공적으로 생성됨",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterUserResponse.class))),
  })
  @PostMapping()
  public ResponseEntity<RegisterUserResponse> register(
      HttpServletRequest request, @RequestParam("uid") String uid) {
    String authorization = request.getHeader("Authorization");
    ArtieToken decodedToken = tokenParsingPort.parseToken(authorization);
    validateUidWithToken(uid, decodedToken);

    return ResponseEntity.status(HttpStatus.CREATED).body(
        registerUserUseCase.register(decodedToken.getUid(), decodedToken.getName(),
            decodedToken.getPicture()));
  }

  private void validateUidWithToken(String uid, ArtieToken decodedToken) {
    if (!decodedToken.getUid().equals(uid)) {
      throw new InvalidValueException();
    }
  }
}
