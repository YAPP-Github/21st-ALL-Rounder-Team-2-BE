package com.yapp.artie.domain.exhibition.domain.exception;

import com.yapp.artie.global.common.exception.BusinessException;
import com.yapp.artie.global.common.exception.ErrorCode;

public class NotOwnerOfExhibitionException extends BusinessException {

  public NotOwnerOfExhibitionException() {
    super(ErrorCode.EXHIBITION_NOT_OWNER);
  }
}
