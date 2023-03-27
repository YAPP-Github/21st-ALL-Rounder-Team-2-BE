package com.yapp.artie.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "회원 Response")
@Getter
public class CreateUserResponseDto {

  @Schema(description = "아이디")
  public Long id;

  public CreateUserResponseDto(Long id) {
    this.id = id;
  }
}
