package com.yapp.artie.domain.user.adapter.out.authentication;

import com.yapp.artie.global.common.exception.BusinessException;
import com.yapp.artie.global.common.exception.ErrorCode;

class InvalidFirebaseUidException extends BusinessException {

  public InvalidFirebaseUidException() {
    super(ErrorCode.FIREBASE_INVALID_UID);
  }
}
