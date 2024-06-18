package store.novabook.store.user.member.exception;

public class MemberAddressNotFoundException extends RuntimeException{
	public MemberAddressNotFoundException() {
		super("해당 멤버의 주소를 찾을 수 없습니다.");
	}
}
