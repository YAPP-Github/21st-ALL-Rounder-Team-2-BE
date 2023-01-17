package com.yapp.artie.domain.archive.exception;

import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class ExceededCategoryCountException extends BusinessException {

  public ExceededCategoryCountException() {
    super(ErrorCode.CATEGORY_EXCEEDED_COUNT);
  }
}
