package com.yapp.artie.domain.user.adapter.out.authentication;

import com.yapp.artie.global.common.exception.BusinessException;
import com.yapp.artie.global.common.exception.ErrorCode;

class FirebaseUserNotFoundException extends BusinessException {

  public FirebaseUserNotFoundException() {
    super(ErrorCode.FIREBASE_NOT_FOUND_USER);
  }

}
