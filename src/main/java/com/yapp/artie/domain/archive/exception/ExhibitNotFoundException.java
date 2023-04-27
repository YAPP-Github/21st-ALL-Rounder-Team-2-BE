package com.yapp.artie.domain.archive.exception;


import com.yapp.artie.global.common.exception.BusinessException;
import com.yapp.artie.global.common.exception.ErrorCode;

public class ExhibitNotFoundException extends BusinessException {

  public ExhibitNotFoundException() {
    super(ErrorCode.EXHIBIT_NOT_FOUND);
  }
}
