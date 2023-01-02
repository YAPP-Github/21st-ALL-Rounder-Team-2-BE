package com.yapp.artie.global.error.exception;

public class NotExistValidTokenException extends BusinessException {

  public NotExistValidTokenException() {
    super(ErrorCode.AUTH_TOKEN_NOT_EXISTS);
  }
}
