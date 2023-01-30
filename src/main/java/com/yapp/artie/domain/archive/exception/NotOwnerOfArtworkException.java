package com.yapp.artie.domain.archive.exception;

import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class NotOwnerOfArtworkException extends BusinessException {

  public NotOwnerOfArtworkException() {
    super(ErrorCode.ARTWORK_NOT_OWNER);
  }
}
