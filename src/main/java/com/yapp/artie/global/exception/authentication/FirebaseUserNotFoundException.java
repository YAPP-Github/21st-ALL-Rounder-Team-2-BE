package com.yapp.artie.global.exception.authentication;

import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class FirebaseUserNotFoundException extends BusinessException {

  public FirebaseUserNotFoundException() {
    super(ErrorCode.FIREBASE_NOT_FOUND_USER);
  }

}
