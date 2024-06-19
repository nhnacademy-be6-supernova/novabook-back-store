package store.novabook.store.user.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import store.novabook.store.user.member.entity.StreetAddress;

@Builder
public record UpdateMemberAddressRequest(

	@NotBlank(message = "별칭은 필수 항목입니다.")
	String nickname,

	@NotBlank(message = "우편번호는 필수 항목입니다.")
	String zipcode,

	@NotBlank(message = "도로명 주소는 필수 항목입니다.")
	String streetAddress,

	@NotBlank(message = "상세주소는 필수 항목입니다.")
	String memberAddressDetail,

	@NotBlank(message = "아이디는 필수 항목입니다.")
	Long memberId

) {
}
