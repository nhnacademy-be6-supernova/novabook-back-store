package store.novabook.store.common.exception;

public class InternalServerException extends NovaException {
	public InternalServerException(ErrorCode errorCode) {
		super(errorCode);
	}
}
