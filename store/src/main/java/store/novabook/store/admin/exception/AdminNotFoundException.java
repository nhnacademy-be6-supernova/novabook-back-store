package store.novabook.store.admin.exception;

public class AdminNotFoundException extends RuntimeException {
	public AdminNotFoundException() {
		super("해당 관리자를 찾을 수 없습니다.");
	}
}
