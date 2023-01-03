package com.yapp.artie.global.exception.authentication;

import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class InvalidTokenException extends BusinessException {

  public InvalidTokenException() {
    super(ErrorCode.FIREBASE_INVALID_TOKEN);
  }
}
