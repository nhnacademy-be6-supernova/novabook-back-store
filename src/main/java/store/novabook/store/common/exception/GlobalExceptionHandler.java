// package store.novabook.store.common.exception;
//
// import java.time.LocalDateTime;
//
// import org.hibernate.exception.ConstraintViolationException;
// import org.springframework.dao.DataIntegrityViolationException;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.HttpStatusCode;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.ResponseStatus;
// import org.springframework.web.bind.annotation.RestControllerAdvice;
// import org.springframework.web.context.request.WebRequest;
// import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
// import jakarta.persistence.PersistenceException;
//
// @RestControllerAdvice
// public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
//
// 	@Override
// 	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
// 		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
// 		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ValidErrorResponse.from(exception));
// 	}
//
// 	@ExceptionHandler(ConstraintViolationException.class)
// 	public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
// 		String errorMessage = "중복된 값 입니다.";
// 		return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorStatus.from(errorMessage, 404,
// 			LocalDateTime.now()));
// 	}
//
// 	@ExceptionHandler(PersistenceException.class)
// 	public ResponseEntity<?> handlePersistenceException(PersistenceException ex, WebRequest request) {
//
// 		ErrorStatus errorStatus = ErrorStatus.from(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
// 			LocalDateTime.now());
//
// 		Throwable cause = ex.getCause();
//
// 		if (cause instanceof ConstraintViolationException constraintViolationException) {
// 			return handleConstraintViolationException(constraintViolationException, request);
// 		}
// 		return new ResponseEntity<>(errorStatus, errorStatus.toHttpStatus());
// 	}
//
// 	@ExceptionHandler(EntityNotFoundException.class)
// 	public ResponseEntity<ErrorStatus> handleApplicationException(EntityNotFoundException e) {
// 		ErrorStatus errorStatus = e.getErrorStatus();
// 		return new ResponseEntity<>(errorStatus, errorStatus.toHttpStatus());
// 	}
//
// 	@ExceptionHandler(AlreadyExistException.class)
// 	public ResponseEntity<ErrorStatus> handleApplicationException(AlreadyExistException e) {
// 		ErrorStatus errorStatus = e.getErrorStatus();
// 		return new ResponseEntity<>(errorStatus, errorStatus.toHttpStatus());
// 	}
//
// 	@ExceptionHandler(NotDeleteCategoryException.class)
// 	public ResponseEntity<ErrorStatus> handleNotDeleteCategoryException(NotDeleteCategoryException e) {
// 		ErrorStatus errorStatus = e.getErrorStatus();
// 		return new ResponseEntity<>(errorStatus, errorStatus.toHttpStatus());
// 	}
//
// 	@ExceptionHandler(Exception.class)
// 	public ResponseEntity<ErrorStatus> handleException(Exception e) {
// 		ErrorStatus errorStatus = new ErrorStatus(e.getMessage(), 500, LocalDateTime.now());
// 		return new ResponseEntity<>(errorStatus, HttpStatus.INTERNAL_SERVER_ERROR);
// 	}
//
// 	@ExceptionHandler(DataIntegrityViolationException.class)
// 	@ResponseStatus(HttpStatus.CONFLICT)
// 	public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
// 		return new ResponseEntity<>("해당 카테고리 등록된 도서가 있어 삭제할 수 없습니다.", HttpStatus.CONFLICT);
// 	}
// }
