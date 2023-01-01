package com.yapp.archiveServer.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus {

  // Common
  UNEXPECTED_SERVER_ERROR("예기치 못한 서버 오류", INTERNAL_SERVER_ERROR),
  INVALID_INPUT_ERROR("잘못된 입력값입니다.", BAD_REQUEST),
  METHOD_NOT_ALLOWED_ERROR(" 지원하지 않는 메서드입니다.", METHOD_NOT_ALLOWED);


  private final String message;
  private final HttpStatus httpStatus;

}