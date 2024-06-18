package store.novabook.handler.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

	private final ErrorStatus errorStatus;

	public ApplicationException(ErrorStatus errorStatus) {
		this.errorStatus = errorStatus;
	}
}
