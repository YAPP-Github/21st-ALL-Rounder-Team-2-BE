package com.yapp.artie.domain.user.adapter.out.authentication.exception;

import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class FirebaseUserNotFoundException extends BusinessException {

  public FirebaseUserNotFoundException() {
    super(ErrorCode.FIREBASE_NOT_FOUND_USER);
  }

}
