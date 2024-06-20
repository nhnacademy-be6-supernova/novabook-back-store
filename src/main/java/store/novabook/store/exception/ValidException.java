// package store.novabook.store.exception;
//
// import java.time.LocalDateTime;
// import java.util.List;
//
// import lombok.Getter;
//
// @Getter
// public class ValidException extends RuntimeException {
// 	private final ErrorStatus errorStatus;
// 	public ValidException(List<?> errors) {
// 		String message = String.format("%s를 찾을 수 없습니다.", entity.getSimpleName());
// 		errorStatus = ErrorStatus.from(message, 404, LocalDateTime.now());
// 	}
// }
