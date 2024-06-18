package store.novabook.store.user.member.dto;

import store.novabook.store.user.member.entity.Member;

public record GetMemberResponse(
	Long id,
	String loginId,
	String name,
	String emial
) {
	public static GetMemberResponse fromEntity(Member member) {
		return new GetMemberResponse(
			member.getId(),
			member.getLoginId(),
			member.getName(),
			member.getEmail());
	}
}
