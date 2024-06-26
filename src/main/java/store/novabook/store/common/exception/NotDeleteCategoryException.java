package store.novabook.store.common.exception;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class NotDeleteCategoryException extends RuntimeException implements Serializable {
	private final ErrorStatus errorStatus;

	public NotDeleteCategoryException() {
		String message = "카테고리 안에 있는 도서가 있어 삭제할 수 없습니다.";
		errorStatus = ErrorStatus.from(message, 400, LocalDateTime.now());
	}

}