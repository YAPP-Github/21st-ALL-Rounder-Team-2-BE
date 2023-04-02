package com.yapp.artie.domain.user.adapter.in.web;


import com.yapp.artie.domain.user.application.port.in.RegisterUserResponse;
import com.yapp.artie.domain.user.application.port.in.RegisterUserUseCase;
import com.yapp.artie.domain.user.application.port.out.TokenParsingPort;
import com.yapp.artie.domain.user.domain.ArtieToken;
import com.yapp.artie.global.annotation.WebAdapter;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@WebAdapter
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class RegisterUserController {

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
