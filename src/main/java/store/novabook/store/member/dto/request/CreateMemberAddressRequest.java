package store.novabook.store.member.dto.request;

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
