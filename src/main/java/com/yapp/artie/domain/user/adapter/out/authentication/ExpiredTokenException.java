package com.yapp.artie.domain.user.adapter.out.authentication;

import com.yapp.artie.global.common.exception.BusinessException;
import com.yapp.artie.global.common.exception.ErrorCode;

class ExpiredTokenException extends BusinessException {

  public ExpiredTokenException() {
    super(ErrorCode.FIREBASE_ACCESS_TOKEN_EXPIRED);
  }
}
