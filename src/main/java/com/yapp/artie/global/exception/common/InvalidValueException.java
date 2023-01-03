package com.yapp.artie.global.exception.common;

import com.yapp.artie.global.exception.response.ErrorCode;

public class InvalidValueException extends BusinessException {

  public InvalidValueException(String value) {
    super(value, ErrorCode.INVALID_INPUT_VALUE);
  }

  public InvalidValueException(String value, ErrorCode errorCode) {
    super(value, errorCode);
  }
}
