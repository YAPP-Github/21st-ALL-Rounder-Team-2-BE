package com.yapp.artie.global.exception.authentication;

import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class RevokedTokenException extends BusinessException {

  public RevokedTokenException() {
    super(ErrorCode.FIREBASE_REVOKED_TOKEN);
  }
}
