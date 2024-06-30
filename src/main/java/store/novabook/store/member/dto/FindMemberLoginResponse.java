package store.novabook.store.member.dto;

public record FindMemberLoginResponse(
	long id,
	String loginId,
	String password,
	String role) {
}
