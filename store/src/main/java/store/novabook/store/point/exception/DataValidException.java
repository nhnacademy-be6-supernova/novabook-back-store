package store.novabook.store.point.exception;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import store.novabook.handler.exception.ErrorStatus;

@RequiredArgsConstructor
public class DataValidException extends RuntimeException {
	private final ErrorStatus errorStatus;

	public DataValidException() {
		String message = "시작 시간이 종료 시간보다 늦습니다.";

		errorStatus = ErrorStatus.from(message, 400, LocalDateTime.now());
	}
}
