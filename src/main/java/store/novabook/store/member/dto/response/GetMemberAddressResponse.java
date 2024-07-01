package store.novabook.store.member.dto.response;

public record GetMemberAddressResponse(
	Long id,
	Long streetAddressId,
	Long memberId,
	String zipcode,
	String nickname,
	String streetAddress,
	String memberAddressDetail
) {
}
