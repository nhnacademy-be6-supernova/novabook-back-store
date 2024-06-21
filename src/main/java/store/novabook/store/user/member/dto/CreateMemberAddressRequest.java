package store.novabook.store.user.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateMemberAddressRequest(

	@NotBlank
	String zipcode,

	@NotNull
	Long memberId,

	@NotBlank
	String streetAddress,

	@NotBlank
	String nickname,

	@NotBlank
	String memberAddressDetail
) {

}
