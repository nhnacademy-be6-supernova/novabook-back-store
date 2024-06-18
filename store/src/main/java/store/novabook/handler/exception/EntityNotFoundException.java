package store.novabook.handler.exception;

public class EntityNotFoundException extends ApplicationException {

	public EntityNotFoundException(ErrorStatus errorStatus) {
		super(errorStatus);
	}
}
