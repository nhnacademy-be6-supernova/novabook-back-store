package store.novabook.store.user.member.dto;

import lombok.Builder;
import store.novabook.store.user.member.entity.Member;

@Builder
public record UpdateMemberResponse(
	String name,
	String loginPassword,
	String number,
	String email
) {
	public static UpdateMemberResponse fromEntity(Member member) {
		return UpdateMemberResponse.builder()
			.name(member.getName())
			.loginPassword(member.getLoginPassword())
			.number(member.getNumber())
			.email(member.getEmail())
			.build();
	}
}
