package com.yapp.artie.global.exception.authentication;

import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class InvalidFirebaseUidException extends BusinessException {

  public InvalidFirebaseUidException() {
    super(ErrorCode.FIREBASE_INVALID_UID);
  }
}
