package store.novabook.store.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationGlobalControllerAdvice  {

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

}
