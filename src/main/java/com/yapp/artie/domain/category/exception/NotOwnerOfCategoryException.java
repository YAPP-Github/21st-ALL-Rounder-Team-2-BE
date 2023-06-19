package com.yapp.artie.domain.category.exception;

import com.yapp.artie.global.common.exception.BusinessException;
import com.yapp.artie.global.common.exception.ErrorCode;

public class NotOwnerOfCategoryException extends BusinessException {

  public NotOwnerOfCategoryException() {
    super(ErrorCode.CATEGORY_NOT_OWNER);
  }
}
