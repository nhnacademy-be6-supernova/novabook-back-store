package store.novabook.store.member.dto;

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
