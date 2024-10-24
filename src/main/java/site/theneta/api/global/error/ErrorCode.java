package site.theneta.api.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "E400", "올바르지 않은 입력값입니다."),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "E401", "인증되지 않은 사용자입니다."),
  PAYMENT_REQUIRED(HttpStatus.PAYMENT_REQUIRED, "E402", "결제가 필요합니다."),
  FORBIDDEN(HttpStatus.FORBIDDEN, "E403", "권한이 없습니다."),
  NOT_FOUND(HttpStatus.NOT_FOUND, "E404", "존재하지 않는 리소스입니다."),
  METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "E405", "잘못된 HTTP 메서드를 호출했습니다."),
  NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, "E406", "허용되지 않는 형식입니다."),
  PROXY_AUTHENTICATION_REQUIRED(HttpStatus.PROXY_AUTHENTICATION_REQUIRED, "E407", "프록시 인증이 필요합니다."),
  REQUEST_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "E408", "요청이 시간초과되었습니다."),
  CONFLICT(HttpStatus.CONFLICT, "E409", "중복된 리소스가 존재합니다."),
  GONE(HttpStatus.GONE, "E410", "요청한 리소스가 삭제되었습니다."),
  LENGTH_REQUIRED(HttpStatus.LENGTH_REQUIRED, "E411", "요청 본문의 길이가 필요합니다."),
  PRECONDITION_FAILED(HttpStatus.PRECONDITION_FAILED, "E412", "사전 조건이 실패했습니다."),
  REQUEST_ENTITY_TOO_LARGE(HttpStatus.REQUEST_ENTITY_TOO_LARGE, "E413", "요청 본문이 너무 큽니다."),
  REQUEST_URI_TOO_LONG(HttpStatus.REQUEST_URI_TOO_LONG, "E414", "요청 URI가 너무 깁니다."),
  UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "E415", "지원되지 않는 미디어 유형입니다."),
  REQUESTED_RANGE_NOT_SATISFIABLE(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE, "E416", "요청한 범위가 만족되지 않습니다."),
  EXPECTATION_FAILED(HttpStatus.EXPECTATION_FAILED, "E417", "예상치 못한 실패가 발생했습니다."),
  UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY, "E422", "처리할 수 없는 엔티티입니다."),
  LOCKED(HttpStatus.LOCKED, "E423", "리소스가 잠겨있습니다."),
  FAILED_DEPENDENCY(HttpStatus.FAILED_DEPENDENCY, "E424", "종속성 오류가 발생했습니다."),
  UPGRADE_REQUIRED(HttpStatus.UPGRADE_REQUIRED, "E426", "업그레이드가 필요합니다."),
  PRECONDITION_REQUIRED(HttpStatus.PRECONDITION_REQUIRED, "E428", "사전 조건이 필요합니다."),
  TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "E429", "요청이 너무 많습니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E500", "서버 에러가 발생했습니다."),

  FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, "A001", "파일 크기가 최대 제한을 초과했습니다"),
  MISSING_REQUEST_HEADER(HttpStatus.BAD_REQUEST, "A002", "필수 요청 헤더가 누락되었습니다"),
  MISSING_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "A003", "필수 요청 매개변수가 누락되었습니다"),
  INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "A004", "유효하지 않은 타입 값입니다"),
  INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, "A005", "유효하지 않은 JSON 형식입니다");

  private final String message;

  private final String code;
  private final HttpStatus status;

  ErrorCode(final HttpStatus status, final String code, final String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }
}
