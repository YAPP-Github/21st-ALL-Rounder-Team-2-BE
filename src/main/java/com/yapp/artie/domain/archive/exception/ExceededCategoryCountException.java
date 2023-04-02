package com.yapp.artie.domain.archive.exception;

import com.yapp.artie.global.common.exception.BusinessException;
import com.yapp.artie.global.common.exception.ErrorCode;

public class ExceededCategoryCountException extends BusinessException {

  public ExceededCategoryCountException() {
    super(ErrorCode.CATEGORY_EXCEEDED_COUNT);
  }
}
