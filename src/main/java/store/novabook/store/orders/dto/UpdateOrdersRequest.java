package store.novabook.store.orders.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateOrdersRequest(
	@NotNull(message = "memberId은 필수 값입니다 ")
	Long memberId,
	@NotNull(message = "deliveryFeeId은 필수 값입니다 ")
	Long deliveryFeeId,
	@NotNull(message = "wrappingPaperId은 필수 값입니다 ")
	Long wrappingPaperId,
	@NotNull(message = "ordersStatusId은 필수 값입니다 ")
	Long ordersStatusId,
	@NotNull(message = "returnPolicyId은 필수 값입니다 ")
	Long returnPolicyId,
	@NotNull(message = "ordersDate은 필수 값입니다 ")
	LocalDateTime ordersDate,
	@NotNull(message = "totalAmount은 필수 값입니다 ")
	Long totalAmount,
	@NotNull(message = "deliveryDate은 필수 값입니다 ")
	LocalDateTime deliveryDate,
	@NotNull(message = "bookPurchaseAmount은 필수 값입니다 ")
	@Min(value = 0, message = "0보다 커야 합니다 ")
	Long bookPurchaseAmount,
	@NotNull(message = "deliveryAddress은 필수 값입니다 ")
	String deliveryAddress,
	@NotBlank(message = "recieverName은 필수 값입니다 ")
	String receiverName,
	@NotBlank(message = "recieverNumber은 필수 값입니다 ")
	String receiverNumber
) {
}
