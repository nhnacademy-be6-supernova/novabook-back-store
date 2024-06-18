package store.novabook.store.exception;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {
	private final ErrorStatus errorStatus;
	public EntityNotFoundException(Class<?> entity) {
		String message = String.format("%s를 찾을 수 없습니다.", entity.getSimpleName());
		errorStatus = ErrorStatus.from(message, 404, LocalDateTime.now());
	}

	public EntityNotFoundException(Class<?> entity, Long id) {
		String message = String.format("아이디가 %s인 %s를 찾을 수 없습니다.", id, entity.getSimpleName());
		errorStatus = ErrorStatus.from(message, 404, LocalDateTime.now());
	}

	public EntityNotFoundException(Long id) {
		String message = String.format("아이디 %s 찾을 수 없습니다.", id);
		this.errorStatus = ErrorStatus.from(message, 404, LocalDateTime.now());
	}
}
