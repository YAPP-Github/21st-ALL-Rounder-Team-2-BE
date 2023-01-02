package com.yapp.artie.global.error.exception;

public class InvalidTokenException extends BusinessException {

  public InvalidTokenException() {
    super(ErrorCode.USER_TOKEN_ERROR);
  }
}
