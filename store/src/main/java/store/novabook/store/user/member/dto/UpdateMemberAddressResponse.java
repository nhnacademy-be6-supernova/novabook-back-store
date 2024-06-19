package store.novabook.store.user.member.dto;

import lombok.Builder;

@Builder
public record UpdateMemberAddressResponse(
	Long id,
	Long streetAddressId,
	Long memberId,
	String nickname,
	String memberAddressDetail
) {

}
