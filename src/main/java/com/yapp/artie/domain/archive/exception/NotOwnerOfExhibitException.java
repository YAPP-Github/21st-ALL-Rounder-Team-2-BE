package com.yapp.artie.domain.archive.exception;

import com.yapp.artie.global.common.exception.BusinessException;
import com.yapp.artie.global.common.exception.ErrorCode;

public class NotOwnerOfExhibitException extends BusinessException {

  public NotOwnerOfExhibitException() {
    super(ErrorCode.EXHIBIT_NOT_OWNER);
  }
}
