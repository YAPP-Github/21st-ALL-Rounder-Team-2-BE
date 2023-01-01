package com.yapp.archiveServer.global.error.exception;

public class InvalidValueException extends BusinessException {

    private InvalidValueException(String value) {
        super(value, ErrorCode.INVALID_INPUT_VALUE);
    }

    public InvalidValueException(String value, ErrorCode errorCode) {
        super(value, errorCode);
    }
}
