package store.novabook.store.member.dto.response;

public record FindMemberLoginResponse(
	long membersId,
	String loginId,
	String loginPassword,
	String role) {
}
