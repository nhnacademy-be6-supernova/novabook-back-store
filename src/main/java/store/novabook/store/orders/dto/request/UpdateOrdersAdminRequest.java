package store.novabook.store.orders.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateOrdersAdminRequest(
	@NotNull(message = "ordersStatusId은 필수 값입니다 ")
	Long ordersStatusId
) {
}
