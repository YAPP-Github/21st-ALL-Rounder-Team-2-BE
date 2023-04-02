package com.yapp.artie.global.common.exception;

public class InvalidValueException extends BusinessException {

  public InvalidValueException() {
    super(ErrorCode.INVALID_INPUT_VALUE);
  }

  public InvalidValueException(String value, ErrorCode errorCode) {
    super(value, errorCode);
  }
}
