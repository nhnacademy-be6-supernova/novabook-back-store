package store.novabook.store.common.exception;

import lombok.Getter;

@Getter
public class FeignClientException extends NovaException {
	private final int status;

	public FeignClientException(int status, ErrorCode errorCode) {
		super(errorCode);
		this.status = status;
	}

}
