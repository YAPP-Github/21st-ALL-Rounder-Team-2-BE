package com.yapp.archiveServer.global.error;

import com.yapp.archiveServer.global.common.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.yapp.archiveServer.global.error.ErrorStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     * HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     * 주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiResponse<?>> handleValidationException(BindingResult bindingResult) {
        return ResponseEntity
                .status(INVALID_INPUT_ERROR.getHttpStatus())
                .body(ApiResponse.createFail(bindingResult));
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ApiResponse<?>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return getApiResponseResponseEntity(METHOD_NOT_ALLOWED_ERROR);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ApiResponse<?>> handleBusinessException(BusinessException e) {
        LOGGER.error(e.getMessage());
        return getApiResponseResponseEntity(e.getErrorStatus());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        LOGGER.error(e.getMessage());
        return getApiResponseResponseEntity(UNEXPECTED_SERVER_ERROR);
    }

    private ResponseEntity<ApiResponse<?>> getApiResponseResponseEntity(ErrorStatus errorStatus) {
        return ResponseEntity
                .status(errorStatus.getHttpStatus())
                .body(ApiResponse.createError(errorStatus.getMessage()));
    }
}
