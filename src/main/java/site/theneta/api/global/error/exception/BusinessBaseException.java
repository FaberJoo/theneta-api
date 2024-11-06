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

  public BusinessBaseException(String message, ErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
    this.target = null;
  }

  public BusinessBaseException(String message, ErrorCode errorCode, String target) {
    super(message);
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
