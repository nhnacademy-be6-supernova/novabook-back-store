package store.novabook.store.orders.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record OrderAddressInfo(
	@Pattern(regexp = "\\d+", message = "우편번호는 숫자만 작성해야합니다.")
	String zipCode,
	@NotBlank
	String streetAddresses,
	String detailAddress
) {
}
