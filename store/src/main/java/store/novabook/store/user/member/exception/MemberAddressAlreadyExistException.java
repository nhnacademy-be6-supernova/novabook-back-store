package store.novabook.store.user.member.exception;

public class MemberAddressAlreadyExistException extends RuntimeException {
	public MemberAddressAlreadyExistException(Long memberId, Long streetAddressId) {
		super(String.format("이미 존재하는 id 입니다", memberId, streetAddressId));
	}
}
