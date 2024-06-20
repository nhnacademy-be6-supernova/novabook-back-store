package store.novabook.store.exception;

import java.time.LocalDateTime;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.persistence.PersistenceException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ValidErrorResponse.from(exception));
	}
	@ExceptionHandler(PersistenceException.class)
	public ResponseEntity<?> handlePersistenceException(PersistenceException ex, WebRequest request) {
		// Hibernate 예외가 래핑되어 있을 수 있음
		Throwable cause = ex.getCause();
		if (cause instanceof ConstraintViolationException) {
			return handleConstraintViolationException((ConstraintViolationException) cause, request);
		}
		return new ResponseEntity<>("Database error", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {

		String errorMessage = "중복된 값 입니다.";
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorStatus.from(errorMessage,404,
			LocalDateTime.now()));

	}
}
