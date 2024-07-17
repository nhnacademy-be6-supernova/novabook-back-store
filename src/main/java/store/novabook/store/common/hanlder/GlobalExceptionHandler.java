package store.novabook.store.common.hanlder;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.FeignClientException;
import store.novabook.store.common.exception.ForbiddenException;
import store.novabook.store.common.exception.KeyManagerException;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.common.exception.NovaException;
import store.novabook.store.common.exception.UnauthorizedException;
import store.novabook.store.common.response.ErrorResponse;
import store.novabook.store.common.response.ValidErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.error("MethodArgumentNotValidException: {} | Location: {}", exception.getMessage(), getLocation(exception),
			exception);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ValidErrorResponse.from(exception));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception,
		WebRequest request) {
		log.error("ConstraintViolationException: {} | Location: {}", exception.getMessage(), getLocation(exception),
			exception);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.from(ErrorCode.DUPLICATED_VALUE));
	}

	@ExceptionHandler(PersistenceException.class)
	public ResponseEntity<ErrorResponse> handlePersistenceException(PersistenceException exception,
		WebRequest request) {
		log.error("PersistenceException: {} | Location: {}", exception.getMessage(), getLocation(exception), exception);
		Throwable cause = exception.getCause();
		if (cause instanceof ConstraintViolationException constraintViolationException) {
			return handleConstraintViolationException(constraintViolationException, request);
		}
		return ResponseEntity.internalServerError().body(ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR));
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> handle(NotFoundException exception, HttpServletRequest request) {
		log.error("NotFoundException: {} | Location: {}", exception.getMessage(), getLocation(exception), exception);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.from(exception));
	}

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ErrorResponse> handle(ForbiddenException exception, HttpServletRequest request) {
		log.error("ForbiddenException: {} | Location: {}", exception.getMessage(), getLocation(exception), exception);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse.from(exception));
	}

	@ExceptionHandler(NovaException.class)
	protected ResponseEntity<Object> handleNovaException(NovaException exception, WebRequest request) {
		log.error("NovaException: {} | Location: {}", exception.getMessage(), getLocation(exception), exception);
		ErrorResponse errorResponse = ErrorResponse.from(exception);
		return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(KeyManagerException.class)
	protected ResponseEntity<Object> handleKeyManagerException(KeyManagerException exception, WebRequest request) {
		log.error("KeyManagerException: {} | Location: {}", exception.getMessage(), getLocation(exception), exception);
		ErrorResponse errorResponse = ErrorResponse.from(exception);
		return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
			request);
	}

	@ExceptionHandler(FeignClientException.class)
	public ResponseEntity<ErrorResponse> handleFeignClientException(FeignClientException exception) {
		log.error("FeignClientException: {} | Location: {}", exception.getMessage(), getLocation(exception), exception);
		return ResponseEntity.status(exception.getStatus()).body(ErrorResponse.from(exception));
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
		DataIntegrityViolationException exception) {
		log.error("DataIntegrityViolationException: {} | Location: {}", exception.getMessage(), getLocation(exception),
			exception);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.from(ErrorCode.ORDER_BOOK_ALREADY_EXISTS));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {
		log.error("MethodArgumentTypeMismatchException: {} | Location: {}", exception.getMessage(),
			getLocation(exception), exception);
		return ResponseEntity.badRequest().body(ErrorResponse.from(ErrorCode.INVALID_ARGUMENT_TYPE));
	}

	@ExceptionHandler(UnauthorizedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException exception) {
		log.error("UnauthorizedException: {} | Location: {}", exception.getMessage(), getLocation(exception),
			exception);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse.from(exception));
	}

	/**
	 * 예외가 발생한 위치 정보를 반환합니다.
	 *
	 * @param exception 예외 객체
	 * @return 예외가 발생한 클래스와 메서드, 라인 정보
	 */
	private String getLocation(Throwable exception) {
		StackTraceElement element = exception.getStackTrace()[0];
		return String.format("%s.%s(%s:%d)", element.getClassName(), element.getMethodName(), element.getFileName(),
			element.getLineNumber());
	}
}
