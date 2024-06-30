package store.novabook.store.user.member.dto;

import lombok.Builder;
import store.novabook.store.user.member.entity.Member;

@Builder
public record GetMemberResponse(
	Long id,
	String loginId,
	String name,
	Integer birthYear,
	Integer birthMonth,
	Integer birthDay,
	String number,
	String email
) {
	public static GetMemberResponse fromEntity(Member member) {
		return GetMemberResponse.builder()
			.id(member.getId())
			.loginId(member.getLoginId())
			.name(member.getName())
			.birthYear(member.getBirth().getYear())
			.birthMonth(member.getBirth().getMonthValue())
			.birthDay(member.getBirth().getDayOfMonth())
			.number(member.getNumber())
			.email(member.getEmail())
			.build();
	}
}
