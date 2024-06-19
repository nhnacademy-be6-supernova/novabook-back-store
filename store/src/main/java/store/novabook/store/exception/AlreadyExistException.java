package store.novabook.store.exception;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class AlreadyExistException extends RuntimeException {
	private final ErrorStatus errorStatus;
	public AlreadyExistException(Class<?> entity) {
		String message = String.format("%s가 이미 존재합니다.", entity.getSimpleName());
		errorStatus = ErrorStatus.from(message, 404, LocalDateTime.now());
	}

	public AlreadyExistException(Class<?> entity, Long id) {
		String message = String.format("아이디가 %s인 %s가 이미 존재합니다.", id, entity.getSimpleName());
		errorStatus = ErrorStatus.from(message, 404, LocalDateTime.now());
	}

	public AlreadyExistException(Long id) {
		String message = String.format("아이디 %s가 이미 존재합니다.", id);
		this.errorStatus = ErrorStatus.from(message, 404, LocalDateTime.now());
	}
}
