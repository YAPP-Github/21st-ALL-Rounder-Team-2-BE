package com.yapp.artie.domain.archive.exception;

import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class NotOwnerOfCategoryException extends BusinessException {

  public NotOwnerOfCategoryException() {
    super(ErrorCode.CATEGORY_NOT_OWNER);
  }
}
