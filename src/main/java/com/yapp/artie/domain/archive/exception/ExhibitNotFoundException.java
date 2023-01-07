package com.yapp.artie.domain.archive.exception;


import com.yapp.artie.global.exception.common.BusinessException;
import com.yapp.artie.global.exception.response.ErrorCode;

public class ExhibitNotFoundException extends BusinessException {

  public ExhibitNotFoundException() {
    super(ErrorCode.EXHIBIT_NOT_FOUND);
  }
}
