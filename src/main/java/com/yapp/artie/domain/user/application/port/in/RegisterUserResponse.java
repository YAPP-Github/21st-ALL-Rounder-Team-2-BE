package com.yapp.artie.domain.user.application.port.in;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "회원 Response")
@Getter
public class RegisterUserResponse {

  @Schema(description = "아이디")
  public Long id;

  public RegisterUserResponse(Long id) {
    this.id = id;
  }
}
