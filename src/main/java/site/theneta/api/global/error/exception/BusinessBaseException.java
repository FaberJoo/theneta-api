package site.theneta.api.global.error.exception;

import site.theneta.api.global.error.ErrorCode;

public class BusinessBaseException extends RuntimeException {

  private final ErrorCode errorCode;
  private final String target;

  public BusinessBaseException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.target = null;
  }

  public BusinessBaseException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
    this.target = null;
  }

  public BusinessBaseException(ErrorCode errorCode, String message, String target) {
    super(message != null ? message : errorCode.getMessage());
    this.errorCode = errorCode;
    this.target = target;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  public String getTarget() {
    return target;
  }
}
