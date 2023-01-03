package com.yapp.artie.global.error.exception.common;

import com.yapp.artie.global.error.response.ErrorCode;

public class EntityNotFoundException extends BusinessException {

  public EntityNotFoundException(String message) {
    super(message, ErrorCode.ENTITY_NOT_FOUND);
  }
}
