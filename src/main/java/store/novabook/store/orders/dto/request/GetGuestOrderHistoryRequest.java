package store.novabook.store.orders.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GetGuestOrderHistoryRequest(

	@NotNull(message = "uuid는 필수 입력값입니다.")
	String uuid,

	@NotBlank(message = "phoneNumber는 필수 입력값입니다.")
	String senderNumber
) {
}
