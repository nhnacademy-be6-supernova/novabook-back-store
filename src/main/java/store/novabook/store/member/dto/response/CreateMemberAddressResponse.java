package store.novabook.store.member.dto.response;

import lombok.Builder;
import store.novabook.store.member.entity.MemberAddress;
import store.novabook.store.member.entity.StreetAddress;

@Builder
public record CreateMemberAddressResponse(Long id, StreetAddress streetAddress, String memberAddressDetail) {

	public static CreateMemberAddressResponse fromEntity(MemberAddress memberAddress) {
		return CreateMemberAddressResponse.builder()
			.id(memberAddress.getId())
			.streetAddress(memberAddress.getStreetAddress())
			.memberAddressDetail(memberAddress.getMemberAddressDetail())
			.build();
	}
}
