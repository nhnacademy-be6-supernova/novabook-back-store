package store.novabook.store.common.exception;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class FailedCreateBookException extends RuntimeException {
	private final ErrorStatus errorStatus;
	public FailedCreateBookException() {
		String message = "도서 저장에 실패해였습니다.";
		errorStatus = ErrorStatus.from(message, 500, LocalDateTime.now());
	}

}
