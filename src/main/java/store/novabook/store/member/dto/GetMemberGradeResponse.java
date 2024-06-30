package store.novabook.store.user.member.dto;

import lombok.Builder;
import store.novabook.store.user.member.entity.MemberGradeHistory;

@Builder
public record GetMemberGradeResponse(
	String name) {
	public static GetMemberGradeResponse from(MemberGradeHistory memberGradeHistory) {
		return GetMemberGradeResponse.builder()
			.name(memberGradeHistory.getMemberGradePolicy().getName())
			.build();
	}
}
