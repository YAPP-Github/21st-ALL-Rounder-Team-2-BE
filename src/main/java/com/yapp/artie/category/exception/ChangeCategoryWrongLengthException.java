package com.yapp.artie.category.exception;

import com.yapp.artie.global.common.exception.BusinessException;
import com.yapp.artie.global.common.exception.ErrorCode;

public class ChangeCategoryWrongLengthException extends BusinessException {

  public ChangeCategoryWrongLengthException() {
    super(ErrorCode.CATEGORY_WRONG_CHANGE_LENGTH);
  }
}
