package com.yapp.archiveServer.global.exception;

public class CustomException extends RuntimeException {

    private final ErrorCode code;

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public ErrorCode getCode() {
        return this.code;
    }

    public CustomException(ErrorCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public CustomException(ErrorCode code, String message) {
        super(code.getMessage() + ": " + message);
        this.code = code;
    }
}