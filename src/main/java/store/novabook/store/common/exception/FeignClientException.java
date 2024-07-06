package store.novabook.store.common.exception;

import lombok.Getter;
import store.novabook.store.common.response.ErrorResponse;

@Getter
public class FeignClientException extends RuntimeException {
	private final int status;
	private final ErrorResponse errorResponse;

	public FeignClientException(int status, ErrorResponse errorResponse) {
		super(errorResponse.message());
		this.status = status;
		this.errorResponse = errorResponse;
	}

}
