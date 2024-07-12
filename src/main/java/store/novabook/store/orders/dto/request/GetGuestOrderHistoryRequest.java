package store.novabook.store.orders.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GetGuestOrderHistoryRequest(

	@NotNull(message = "ordersId는 필수 입력값입니다.")
	Long ordersId,

	@NotBlank(message = "phoneNumber는 필수 입력값입니다.")
	String phoneNumber
) {
}
