package store.novabook.store.user.member.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record GetMemberAddressListResponse(
	List<GetMemberAddressResponse> memberAddresses
) {
}
