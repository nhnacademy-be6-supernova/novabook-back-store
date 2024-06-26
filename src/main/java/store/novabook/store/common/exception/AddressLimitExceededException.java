package store.novabook.store.common.exception;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class AddressLimitExceededException extends RuntimeException {
	private final ErrorStatus errorStatus;

	public AddressLimitExceededException(Long id) {
		String message = String.format("id - %s : 주소는 10개까지만 등록 가능합니다.",id);
		errorStatus = ErrorStatus.from(message, 400, LocalDateTime.now());
	}

}