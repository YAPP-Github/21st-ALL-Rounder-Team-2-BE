package com.yapp.artie.domain.archive.exception;

import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class ChangeCategoryWrongLengthException extends BusinessException {

  public ChangeCategoryWrongLengthException() {
    super(ErrorCode.CATEGORY_WRONG_CHANGE_LENGTH);
  }
}
