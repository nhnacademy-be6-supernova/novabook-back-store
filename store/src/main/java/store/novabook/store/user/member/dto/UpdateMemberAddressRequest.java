package store.novabook.store.user.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import store.novabook.store.user.member.entity.StreetAddress;

@Builder
public record UpdateMemberAddressRequest(

	@NotBlank
	String nickname,

	@NotBlank
	String zipcode,

	@NotBlank
	Long streetAddressId,

	@NotBlank
	String memberAddressDetail

) {
}
