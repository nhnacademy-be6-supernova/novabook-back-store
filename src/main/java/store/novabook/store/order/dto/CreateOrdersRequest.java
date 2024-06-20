package store.novabook.store.order.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateOrdersRequest (
	@NotNull
	Long userId,
	@NotNull
	Long deliveryFeeId,
	@NotNull
	Long wrappingPaperId,
	@NotNull
	Long ordersStatusId,
	@NotNull
	Long returnPolicyId,
	@NotNull
	LocalDateTime ordersDate,
	@NotNull
	Long totalAmount,
	@NotNull
	LocalDateTime deliveryDate,
	@NotNull
	long bookPurchaseAmount,
	@NotNull
	String deliveryAddress,
	@NotNull
	@NotBlank
	String recieverName,
	@NotNull
	@NotBlank
	String recieverNumber
){
}
