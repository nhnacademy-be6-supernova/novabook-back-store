package store.novabook.store.user.member.dto;

public record FindMemberLoginResponse(
	String loginId,
	String password,
	String role) {
}
