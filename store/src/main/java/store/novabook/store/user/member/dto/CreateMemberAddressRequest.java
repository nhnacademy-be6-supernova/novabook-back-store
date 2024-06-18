package store.novabook.store.user.member.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateMemberAddressRequest(

	@NotBlank
	String zipcode,

	@NotBlank
	Long memberId,

	@NotBlank
	String streetAddress,

	@NotBlank
	String nickName,

	@NotBlank
	String memberAddressDetail
) {

}
