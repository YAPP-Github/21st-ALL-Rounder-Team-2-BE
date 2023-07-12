package com.yapp.artie.category.domain.exception;

import com.yapp.artie.global.common.exception.BusinessException;
import com.yapp.artie.global.common.exception.ErrorCode;

public class InvalidSequenceException extends BusinessException {

  public InvalidSequenceException() {
    super(ErrorCode.CATEGORY_INVALID_SEQUENCE);
  }
}

