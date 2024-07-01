package store.novabook.store.member.dto.response;

public record FindMemberLoginResponse(
	long id,
	String loginId,
	String password,
	String role) {
}
