package store.novabook.store.user.member.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateMemberAddressRequest(

	@NotBlank
	String zipcode,

	@NotBlank
	String streetAddress,

	@NotBlank
	String nickname,

	@NotBlank
	String memberAddressDetail
) {

}
