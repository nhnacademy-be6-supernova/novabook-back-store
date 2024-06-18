package store.novabook.store.admin.exception;

public class AdminAlreadyExistsException extends RuntimeException {
	public AdminAlreadyExistsException(Long id) {
		super(String.format("이미 존재하는 id 입니다", id));
	}
}
