package store.novabook.store.exception;

public class InternalServerException extends NovaException {
	public InternalServerException(ErrorCode errorCode) {
		super(errorCode);
	}
}
