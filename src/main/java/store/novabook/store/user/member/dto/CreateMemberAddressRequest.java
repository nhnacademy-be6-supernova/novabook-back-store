package store.novabook.store.user.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateMemberAddressRequest(

	@NotBlank
	String zipcode,

	@NotBlank
	Long memberId,

	@NotBlank
	String streetAddress,

	@NotBlank
	String nickname,

	@NotBlank
	String memberAddressDetail
) {

}
