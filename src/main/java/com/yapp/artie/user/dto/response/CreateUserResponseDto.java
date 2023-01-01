package com.yapp.artie.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "회원 Response")
public class CreateUserResponseDto {

  @Schema(description = "아이디")
  public Long id;

  public CreateUserResponseDto(Long id) {
    this.id = id;
  }
}
