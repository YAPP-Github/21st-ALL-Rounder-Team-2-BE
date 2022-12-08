package com.yapp.archiveServer.global.error;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {

    private final ErrorStatus errorStatus;

    public BusinessException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }

    public BusinessException(ErrorStatus responseStatus, String message) {
        super(message);
        this.errorStatus = responseStatus;
    }

    public ErrorStatus getErrorStatus() {
        return errorStatus;
    }

    public HttpStatus getHttpStatus() {
        return errorStatus.getHttpStatus();
    }

}
