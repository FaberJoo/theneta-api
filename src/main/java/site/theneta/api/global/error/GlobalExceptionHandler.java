package site.theneta.api.global.error;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import jakarta.validation.ConstraintViolationException;

import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.fasterxml.jackson.core.JsonParseException;

import lombok.extern.slf4j.Slf4j;
import site.theneta.api.global.response.ErrorResponse;
import site.theneta.api.global.error.exception.BusinessBaseException;

@Slf4j
@ControllerAdvice // 모든 컨트롤러에서 발생하는 예외를 잡아서 처리
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final Environment environment;

  /**
   * 요청 데이터 유효성 검증 실패시 발생하는 예외 처리
   * 예시: @Valid 어노테이션으로 검증 실패한 경우
   * - 필수 필드 누락
   * - 잘못된 형식의 데이터
   * - @Size, @NotNull 등의 제약조건 위반
   * 응답: 400 Bad Request
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException e) {
    log.error("Method Argument Not Valid Exception: {}", e.getMessage());
    return new ResponseEntity<>(
        ErrorResponse.of(ErrorCode.INVALID_REQUEST_PARAMETER, e.getBindingResult()),
        HttpStatus.BAD_REQUEST);
  }

  /**
   * Bean Validation 제약조건 위반 예외 처리
   * 예시:
   * - @PathVariable, @RequestParam 등의 검증 실패
   * - Custom validation 실패
   * 응답: 400 Bad Request
   */
  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<ErrorResponse> handle(ConstraintViolationException e) {
    log.error("Constraint Violation Exception: {}", e.getMessage());
    return new ResponseEntity<>(
            ErrorResponse.of(ErrorCode.INVALID_REQUEST_PARAMETER, e.getConstraintViolations()),
            HttpStatus.BAD_REQUEST);
  }

  /**
   * HTTP 요청 본문을 읽을 수 없는 경우의 예외 처리
   * 예시:
   * - 잘못된 JSON 형식
   * - 요청 본문이 비어있는 경우
   * - Content-Type과 실제 본문 형식이 불일치하는 경우
   * 응답: 400 Bad Request
   */
  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException e) {
    log.error("Http Message Not Readable Exception: {}", e.getMessage(), e);

    return createErrorResponseEntity(ErrorCode.INVALID_INPUT_VALUE);
  }

  /**
   * 데이터베이스 제약조건 위반 예외 처리
   * 예시:
   * - Unique 제약조건 위반 (중복된 이메일 등록 시도)
   * - Not Null 제약조건 위반
   * - Foreign Key 제약조건 위반
   * 응답: 400 Bad Request
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  protected ResponseEntity<ErrorResponse> handle(DataIntegrityViolationException e) {
    log.error("Data Integrity Violation Exception: {}", e.getMessage(), e);
    return createErrorResponseEntity(ErrorCode.INVALID_INPUT_VALUE);
  }

  /**
   * 파일 업로드 크기 제한 초과 예외 처리
   * 예시:
   * - 설정된 최대 파일 크기보다 큰 파일 업로드 시도
   * - 여러 파일의 총 크기가 제한을 초과하는 경우
   * 응답: 400 Bad Request
   */
  @ExceptionHandler(MaxUploadSizeExceededException.class)
  protected ResponseEntity<ErrorResponse> handle(MaxUploadSizeExceededException e) {
    log.error("File Size Exceeded Exception: {}", e.getMessage(), e);
    return createErrorResponseEntity(ErrorCode.FILE_SIZE_EXCEEDED);
  }

  /**
   * 필수 요청 헤더 누락 예외 처리
   * 예시:
   * - Authorization 헤더 누락
   * - Custom 필수 헤더 누락
   * 응답: 400 Bad Request
   */
  @ExceptionHandler(MissingRequestHeaderException.class)
  protected ResponseEntity<ErrorResponse> handle(MissingRequestHeaderException e) {
    log.error("Missing Request Header Exception: {}", e.getMessage(), e);
    return createErrorResponseEntity(ErrorCode.MISSING_REQUEST_HEADER);
  }

  /**
   * 필수 요청 파라미터 누락 예외 처리
   * 예시:
   * - @RequestParam으로 지정된 필수 파라미터 누락
   * - Query string 파라미터 누락
   * 응답: 400 Bad Request
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  protected ResponseEntity<ErrorResponse> handle(MissingServletRequestParameterException e) {
    log.error("Missing Request Parameter Exception: {}", e.getMessage(), e);
    return createErrorResponseEntity(ErrorCode.MISSING_REQUEST_PARAMETER);
  }

  /**
   * Handler Method Validation 실패 예외 처리
   * 예시:
   * - 컨트롤러 메소드의 파라미터 검증 실패
   * - Path Variable, Request Parameter 검증 실패
   * 응답: 400 Bad Request
   */
  @ExceptionHandler(HandlerMethodValidationException.class)
  protected ResponseEntity<ErrorResponse> handle(HandlerMethodValidationException e) {
    log.error("Handler Method Validation Exception: {}", e.getMessage(), e);
    return createErrorResponseEntity(ErrorCode.INVALID_INPUT_VALUE);
  }

  /**
   * URL 경로 변수의 타입 변환 실패 예외 처리
   * 예시:
   * - /api/users/{id}에서 id를 Long으로 변환 실패
   * - 날짜 형식 변환 실패
   * - Enum 타입 변환 실패
   * 응답: 400 Bad Request
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  protected ResponseEntity<ErrorResponse> handle(MethodArgumentTypeMismatchException e) {
    log.error("Method Argument Type Mismatch: {}", e.getMessage(), e);
    return createErrorResponseEntity(ErrorCode.INVALID_TYPE_VALUE);
  }

  /**
   * JSON 파싱 실패 예외 처리
   * 예시:
   * - 잘못된 JSON 형식
   * - JSON 데이터 타입 불일치
   * 응답: 400 Bad Request
   */
  @ExceptionHandler(JsonParseException.class)
  protected ResponseEntity<ErrorResponse> handle(JsonParseException e) {
    log.error("JSON Parse Exception: {}", e.getMessage(), e);
    return createErrorResponseEntity(ErrorCode.INVALID_JSON_FORMAT);
  }

  /**
   * 요청한 리소스를 찾을 수 없는 경우의 예외 처리
   * 예시:
   * - 존재하지 않는 API 엔드포인트 호출
   * - 잘못된 URL 경로
   * 응답: 404 Not Found
   */
  @ExceptionHandler(NoHandlerFoundException.class)
  protected ResponseEntity<ErrorResponse> handle(NoHandlerFoundException e) {
    log.error("No Handler Found: {}", e.getMessage(), e);
    return createErrorResponseEntity(ErrorCode.NOT_FOUND);
  }

  /**
   * 정적 리소스를 찾을 수 없는 경우의 예외 처리
   * 예시:
   * - 존재하지 않는 경로로 요청
   * - 잘못된 URL 패턴
   * 응답: 404 Not Found
   */
  @ExceptionHandler(NoResourceFoundException.class)
  protected ResponseEntity<ErrorResponse> handle(NoResourceFoundException e) {
    log.error("No Resource Found Exception: {}", e.getMessage(), e);
    return createErrorResponseEntity(ErrorCode.NOT_FOUND);
  }

  /**
   * HTTP 메소드가 잘못된 경우 발생하는 예외 처리
   * 예시: POST 요청을 해야하는 엔드포인트에 GET 요청을 보낸 경우
   * 응답: 405 Method Not Allowed
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class) // HttpRequestMethodNotSupportedException 예외를 잡아서 처리
  protected ResponseEntity<ErrorResponse> handle(HttpRequestMethodNotSupportedException e) {
    log.error("Http Request Method Not Supported Exception", e);
    return createErrorResponseEntity(ErrorCode.METHOD_NOT_ALLOWED);
  }

  /**
   * 비즈니스 로직 수행 중 발생하는 예외 처리
   * 예시:
   * - 이미 존재하는 회원 가입 시도
   * - 잘못된 비밀번호 입력
   * - 권한이 없는 리소스 접근
   * - 존재하지 않는 데이터 조회
   * 응답: ErrorCode에 정의된 상태코드 (400, 401, 403, 404 등)
   */
  @ExceptionHandler(BusinessBaseException.class)
  protected ResponseEntity<ErrorResponse> handle(BusinessBaseException e) {
    log.error("Business Exception: code={}, message={}, target={}",
        e.getErrorCode(), e.getMessage(), e.getTarget(), e);
    return createErrorResponseEntity(e.getErrorCode(), e.getTarget(), e.getMessage());
  }

  /**
   * 위에서 처리되지 않은 모든 예외를 처리하는 핸들러
   * 예시:
   * - NullPointerException
   * - IllegalArgumentException
   * - 데이터베이스 연결 오류
   * - 외부 API 호출 실패
   * 응답: 500 Internal Server Error
   * 참고: 개발 환경('dev' 프로필)에서만 스택트레이스 출력
   */
  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handle(Exception e) {
    log.error("Unhandled Exception: {}", e.getMessage(), e);
    if (environment.acceptsProfiles(Profiles.of("dev"))) {
      e.printStackTrace();
    }
    return createErrorResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
  }

  /**
   * ErrorCode 기반으로 ErrorResponse 생성
   * 
   * @param errorCode 에러 코드 enum
   * @return ResponseEntity<ErrorResponse>
   */
  private ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode) {
    return new ResponseEntity<>(
        ErrorResponse.of(errorCode),
        errorCode.getStatus());
  }

  private ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode, String message, String target) {
    return new ResponseEntity<>(
        ErrorResponse.of(errorCode, target, message),
        errorCode.getStatus());
  }
}
