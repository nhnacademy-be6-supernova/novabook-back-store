package store.novabook.store.user.member.exception;

public class MemberAlreadyExistsException extends RuntimeException {
	public MemberAlreadyExistsException(Long id) {
		super(String.format("이미 존재하는 id 입니다", id));
	}
}
