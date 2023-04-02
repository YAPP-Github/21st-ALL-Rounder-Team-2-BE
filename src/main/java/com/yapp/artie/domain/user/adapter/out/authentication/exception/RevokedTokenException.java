package com.yapp.artie.domain.user.adapter.out.authentication.exception;

import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class RevokedTokenException extends BusinessException {

  public RevokedTokenException() {
    super(ErrorCode.FIREBASE_REVOKED_TOKEN);
  }
}
