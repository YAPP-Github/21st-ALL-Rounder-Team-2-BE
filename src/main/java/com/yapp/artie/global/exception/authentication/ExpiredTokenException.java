package com.yapp.artie.global.exception.authentication;

import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class ExpiredTokenException extends BusinessException {

  public ExpiredTokenException() {
    super(ErrorCode.FIREBASE_ACCESS_TOKEN_EXPIRED);
  }
}
