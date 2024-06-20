package store.novabook.store.common.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler  {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ValidErrorResponse.from(exception));
	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorStatus> handleApplicationException(EntityNotFoundException e) {
		ErrorStatus errorStatus = e.getErrorStatus();
		return new ResponseEntity<>(errorStatus, errorStatus.toHttpStatus());
	}
	@ExceptionHandler(AlreadyExistException.class)
	public ResponseEntity<ErrorStatus> handleApplicationException(AlreadyExistException e) {
		ErrorStatus errorStatus = e.getErrorStatus();
		return new ResponseEntity<>(errorStatus, errorStatus.toHttpStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorStatus> handleException(Exception e) {
		ErrorStatus errorStatus = new ErrorStatus(e.getMessage(), 500, LocalDateTime.now());
		return new ResponseEntity<>(errorStatus, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
