package store.novabook.store.user.member.dto;

public record FindMemberLoginResponse(
	long id,
	String loginId,
	String password,
	String role) {
}
