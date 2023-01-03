package com.yapp.artie.global.exception.authentication;

import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class NotExistValidTokenException extends BusinessException {

  public NotExistValidTokenException() {
    super(ErrorCode.FIREBASE_TOKEN_NOT_EXISTS);
  }
}
