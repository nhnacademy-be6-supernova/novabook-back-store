package store.novabook.store.user.member.dto;

import lombok.Builder;
import store.novabook.store.user.member.entity.Member;

@Builder
public record CreateMemberResponse(Long id) {
	public static CreateMemberResponse fromEntity(Member member) {
		return CreateMemberResponse.builder()
			.id(member.getId())
			.build();
	}
}
