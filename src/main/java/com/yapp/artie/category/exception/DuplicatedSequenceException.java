package com.yapp.artie.category.exception;

import com.yapp.artie.global.common.exception.BusinessException;
import com.yapp.artie.global.common.exception.ErrorCode;

public class DuplicatedSequenceException extends BusinessException {

  public DuplicatedSequenceException() {
    super(ErrorCode.CATEGORY_SEQUENCE_DUPLICATED);
  }
}
