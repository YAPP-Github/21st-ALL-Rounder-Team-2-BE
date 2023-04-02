package com.yapp.artie.domain.user.adapter.out.authentication;

import com.yapp.artie.global.common.exception.BusinessException;
import com.yapp.artie.global.common.exception.ErrorCode;

class InvalidTokenException extends BusinessException {

  public InvalidTokenException() {
    super(ErrorCode.FIREBASE_INVALID_TOKEN);
  }
}
