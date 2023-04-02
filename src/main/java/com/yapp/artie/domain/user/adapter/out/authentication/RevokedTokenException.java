package com.yapp.artie.domain.user.adapter.out.authentication;

import com.yapp.artie.global.common.exception.BusinessException;
import com.yapp.artie.global.common.exception.ErrorCode;

class RevokedTokenException extends BusinessException {

  public RevokedTokenException() {
    super(ErrorCode.FIREBASE_REVOKED_TOKEN);
  }
}
