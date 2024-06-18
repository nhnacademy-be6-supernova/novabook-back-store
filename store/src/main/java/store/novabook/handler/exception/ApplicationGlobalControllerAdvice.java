package store.novabook.handler.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationGlobalControllerAdvice {

	@ExceptionHandler(ApplicationException.class)
	public ResponseEntity<ErrorStatus> handleApplicationException(ApplicationException e) {
		ErrorStatus errorStatus = e.getErrorStatus();

		return new ResponseEntity<>(errorStatus, errorStatus.toHttpStatus());
	}
}
