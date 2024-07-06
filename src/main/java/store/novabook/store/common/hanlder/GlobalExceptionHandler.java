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
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.FeignClientException;
import store.novabook.store.common.exception.ForbiddenException;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.common.exception.NovaException;
import store.novabook.store.common.response.ErrorResponse;
import store.novabook.store.common.response.ValidErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ValidErrorResponse.from(exception));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex,
		WebRequest request) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.from(ErrorCode.DUPLICATED_VALUE));
	}

	@ExceptionHandler(PersistenceException.class)
	public ResponseEntity<ErrorResponse> handlePersistenceException(PersistenceException ex, WebRequest request) {
		Throwable cause = ex.getCause();
		if (cause instanceof ConstraintViolationException constraintViolationException) {
			return handleConstraintViolationException(constraintViolationException, request);
		}
		return ResponseEntity.internalServerError().body(ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR));
	}

	/**
	 * {@code NotFoundException}을 처리하여 {@link ErrorResponse}를 반환합니다.
	 *
	 * @param exception {@code NotFoundException}
	 * @param request   HTTP 요청
	 * @return {@link ErrorResponse}를 포함하는 {@link ResponseEntity} 객체
	 */
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> handle(NotFoundException exception, HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.from(exception));
	}

	/**
	 * {@code ForbiddenException}을 처리하여 {@link ErrorResponse}를 반환합니다.
	 *
	 * @param exception {@code ForbiddenException}
	 * @param request   HTTP 요청
	 * @return {@link ErrorResponse}를 포함하는 {@link ResponseEntity} 객체
	 */
	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ErrorResponse> handle(ForbiddenException exception, HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse.from(exception));
	}

	/**
	 * {@code NovaException}을 처리하여 {@link ErrorResponse}를 반환합니다.
	 *
	 * @param ex      {@code NovaException}
	 * @param request 웹 요청
	 * @return {@link ErrorResponse}를 포함하는 {@link ResponseEntity} 객체
	 */
	@ExceptionHandler(NovaException.class)
	protected ResponseEntity<Object> handleNovaException(NovaException ex, WebRequest request) {
		ErrorResponse errorResponse = ErrorResponse.from(ex);
		return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(FeignClientException.class)
	public ResponseEntity<ErrorResponse> handleFeignClientException(FeignClientException e) {
		// ErrorResponse<CouponErrorBody> errorResponse = e.getErrorResponse();
		// return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse.getBody());
		return null;
	}

	// /**
	//  * 일반 {@code Exception}을 처리하여 {@link ErrorResponse}를 반환합니다.
	//  *
	//  * @param exception 일반 {@code Exception}
	//  * @param request   HTTP 요청
	//  * @return {@link ErrorResponse}를 포함하는 {@link ResponseEntity} 객체
	//  */
	// @ExceptionHandler(Exception.class)
	// public ResponseEntity<ErrorResponse> handleException(Exception exception, HttpServletRequest request) {
	// 	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	// 		.body(ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR));
	// }

	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.from(ErrorCode.ORDER_BOOK_ALREADY_EXISTS));
	}

	/**
	 * {@code MethodArgumentTypeMismatchException}을 처리하여 {@link ErrorResponse}를 반환합니다.
	 *
	 * @param ex {@code MethodArgumentTypeMismatchException}
	 * @return {@link ErrorResponse}를 포함하는 {@link ResponseEntity} 객체
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
		return ResponseEntity.badRequest().body(ErrorResponse.from(ErrorCode.INVALID_ARGUMENT_TYPE));
	}

}
