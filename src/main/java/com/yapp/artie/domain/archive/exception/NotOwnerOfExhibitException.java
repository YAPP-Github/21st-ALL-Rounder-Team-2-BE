package com.yapp.artie.domain.archive.exception;

import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class NotOwnerOfExhibitException extends BusinessException {

  public NotOwnerOfExhibitException() {
    super(ErrorCode.EXHIBIT_NOT_OWNER);
  }
}
