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
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.common.exception.NovaException;
import store.novabook.store.common.response.ErrorResponse;
import store.novabook.store.common.response.ValidErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.error("MethodArgumentNotValidException: {}", exception.getMessage(), exception);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ValidErrorResponse.from(exception));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception,
		WebRequest request) {
		log.error("ConstraintViolationException: {}", exception.getMessage(), exception);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.from(ErrorCode.DUPLICATED_VALUE));
	}

	@ExceptionHandler(PersistenceException.class)
	public ResponseEntity<ErrorResponse> handlePersistenceException(PersistenceException exception,
		WebRequest request) {
		log.error("PersistenceException: {}", exception.getMessage(), exception);
		Throwable cause = exception.getCause();
		if (cause instanceof ConstraintViolationException constraintViolationException) {
			return handleConstraintViolationException(constraintViolationException, request);
		}
		return ResponseEntity.internalServerError().body(ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR));
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> handle(NotFoundException exception, HttpServletRequest request) {
		log.warn("NotFoundException: {}", exception.getMessage(), exception);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.from(exception));
	}

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ErrorResponse> handle(ForbiddenException exception, HttpServletRequest request) {
		log.warn("ForbiddenException: {}", exception.getMessage(), exception);
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse.from(exception));
	}

	@ExceptionHandler(NovaException.class)
	protected ResponseEntity<Object> handleNovaException(NovaException exception, WebRequest request) {
		log.warn("NovaException: {}", exception.getMessage(), exception);
		ErrorResponse errorResponse = ErrorResponse.from(exception);
		return handleExceptionInternal(exception, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(FeignClientException.class)
	public ResponseEntity<ErrorResponse> handleFeignClientException(FeignClientException exception) {
		log.error("FeignClientException: {}", exception.getMessage(), exception);
		return ResponseEntity.status(exception.getStatus()).body(ErrorResponse.from(exception));
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
		DataIntegrityViolationException exception) {
		log.error("DataIntegrityViolationException: {}", exception.getMessage(), exception);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.from(ErrorCode.ORDER_BOOK_ALREADY_EXISTS));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {
		log.error("MethodArgumentTypeMismatchException: {}", exception.getMessage(), exception);
		return ResponseEntity.badRequest().body(ErrorResponse.from(ErrorCode.INVALID_ARGUMENT_TYPE));
	}

}
