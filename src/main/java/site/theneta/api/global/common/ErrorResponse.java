package site.theneta.api.global.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.theneta.api.global.error.ErrorCode;
import org.springframework.validation.BindingResult;
import jakarta.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {

  private String code;
  private String target;
  private String message;
  private List<FieldError> errors;

  private ErrorResponse(final ErrorCode code) {
    this.message = code.getMessage();
    this.code = code.getCode();
  }

  public ErrorResponse(final ErrorCode code, final String message) {
    this.message = message;
    this.code = code.getCode();
  }

  public ErrorResponse(final ErrorCode code, final String target, final String message) {
    this.code = code.getCode();
    this.target = target;
    this.message = message;
  }

  // BindingResult와 ConstraintViolation을 위한 생성자 추가
  private ErrorResponse(final ErrorCode code, final List<FieldError> errors) {
    this.message = code.getMessage();
    this.code = code.getCode();
    this.errors = errors; // 필드 에러 리스트 설정
  }

  public static ErrorResponse of(final ErrorCode code) {
    return new ErrorResponse(code);
  }

  public static ErrorResponse of(final ErrorCode code, final String message) {
    return new ErrorResponse(code, message);
  }

  public static ErrorResponse of(final ErrorCode code, final String message, final String target) {
    return new ErrorResponse(code, target, message);
  }

  public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
    return new ErrorResponse(code, FieldError.of(bindingResult));
  }

  public static ErrorResponse of(final ErrorCode code, final Set<ConstraintViolation<?>> violations) {
    return new ErrorResponse(code, FieldError.of(violations));
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

    private static List<FieldError> of(final Set<ConstraintViolation<?>> violations) {
      return violations.stream()
          .map(violation -> new FieldError(
              violation.getPropertyPath().toString(),
              violation.getInvalidValue() == null ? "" : violation.getInvalidValue().toString(),
              violation.getMessage()))
          .collect(Collectors.toList());
    }
  }
}
