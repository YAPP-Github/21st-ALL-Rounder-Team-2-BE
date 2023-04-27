package com.yapp.artie.global.common.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

  private String message;
  private int status;
  private List<FieldError> errors;
  private String code;

  private ErrorResponse(final ErrorCode code, final List<FieldError> errors) {
    this.message = code.getMessage();
    this.status = code.getStatus();
    this.errors = errors;
    this.code = code.getCode();
  }

  private ErrorResponse(final ErrorCode code) {
    this.message = code.getMessage();
    this.status = code.getStatus();
    this.code = code.getCode();
    this.errors = new ArrayList<>();
  }

  public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
    return new ErrorResponse(code, FieldError.of(bindingResult));
  }

  public static ErrorResponse of(final ErrorCode code) {
    return new ErrorResponse(code);
  }

  public static ErrorResponse of(final ErrorCode code, final List<FieldError> errors) {
    return new ErrorResponse(code, errors);
  }

  public static ErrorResponse of(MethodArgumentTypeMismatchException e) {
    final String value = e.getValue() == null ? "" : e.getValue().toString();
    final List<ErrorResponse.FieldError> errors = ErrorResponse.FieldError.of(e.getName(), value,
        e.getErrorCode());
    return new ErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors);
  }

  public static ErrorResponse of(HttpMessageNotReadableException e) {
    return new ErrorResponse(ErrorCode.INVALID_INPUT_VALUE);
  }

  public static ErrorResponse of(InvalidFormatException e) {
    List<ErrorResponse.FieldError> errors = ErrorResponse.FieldError.of(
        e.getPath().size() > 0 ? e.getPath().get(0).getFieldName() : "알 수 없는 필드",
        e.getValue().toString(),
        "해당 필드는 " + e.getTargetType().getSimpleName() + " 타입이어야합니다.");
    return new ErrorResponse(ErrorCode.INVALID_TYPE_VALUE, errors);
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class FieldError {

    private String field;
    private String value;
    private String reason;

    private FieldError(final String field, final String value, final String reason) {
      this.field = field;
      this.value = value;
      this.reason = reason;
    }

    public static List<FieldError> of(final String field, final String value, final String reason) {
      List<FieldError> fieldErrors = new ArrayList<>();
      fieldErrors.add(new FieldError(field, value, reason));
      return fieldErrors;
    }

    private static List<FieldError> of(final BindingResult bindingResult) {
      final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
      return fieldErrors.stream()
          .map(error -> new FieldError(
              error.getField(),
              error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
              error.getDefaultMessage()))
          .collect(Collectors.toList());
    }
  }
}
