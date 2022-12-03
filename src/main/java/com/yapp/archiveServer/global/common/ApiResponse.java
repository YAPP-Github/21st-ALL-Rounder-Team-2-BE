package com.yapp.archiveServer.global.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {

    public static final String API_CALL_SUCCESS_MESSAGE = "API가 정상적으로 호출되었습니다.";
    public static final String API_CALL_FAIL_MESSAGE = "API 호출을 실패했습니다.";

    private boolean success;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime timestamp;
    private T data;

    public ApiResponse(boolean success, T data, String message) {
        this.success = success;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.data = data;
    }

    public static <T> ApiResponse<T> createSuccess(T data) {
        return new ApiResponse<>(true, data, API_CALL_SUCCESS_MESSAGE);
    }

    public static ApiResponse<?> createSuccessWithNoContent() {
        return new ApiResponse<>(true, null, API_CALL_SUCCESS_MESSAGE);
    }

    public static ApiResponse<?> createFail(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        List<ObjectError> allErrors = bindingResult.getAllErrors();
        for (ObjectError error : allErrors) {
            if (error instanceof FieldError) {
                errors.put(((FieldError) error).getField(), error.getDefaultMessage());
            } else {
                errors.put(error.getObjectName(), error.getDefaultMessage());
            }
        }
        return new ApiResponse<>(false, errors, API_CALL_FAIL_MESSAGE);
    }

    public static ApiResponse<?> createError(String message) {
        return new ApiResponse<>(false, null, message);
    }
}
