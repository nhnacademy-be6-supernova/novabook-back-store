package store.novabook.store.orders.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateOrdersRequest(
	@NotNull(message = "ordersBookId 값은 필수 입니다")
	Long memberId,
	@NotNull(message = "deliveryFeeId 값은 필수 입니다")
	Long deliveryFeeId,
	@NotNull(message = "wrappingPaperId 값은 필수 입니다")
	Long wrappingPaperId,
	@NotNull(message = "ordersStatusId 값은 필수 입니다")
	Long ordersStatusId,
	Long paymentId,
	@NotNull(message = "ordersDate 값은 필수 입니다")
	LocalDateTime ordersDate,
	String uuid,
	@NotNull(message = "totalAmount 값은 필수 입니다")
	Long totalAmount,
	@NotNull(message = "deliveryDate 값은 필수 입니다")
	LocalDateTime deliveryDate,
	@NotNull(message = "bookPurchaseAmount 값은 필수 입니다")
	@Min(value = 0, message = "0보다 커야 합니다 ")
	Long bookPurchaseAmount,
	@NotBlank(message = "deliveryAddress 값은 필수 입니다")
	String deliveryAddress,
	@NotNull
	String senderName,
	@NotNull
	String senderNumber,
	@NotBlank(message = "recieverName 값은 필수 입니다")
	String receiverName,
	@NotBlank(message = "recieverNumber 값은 필수 입니다")
	String receiverNumber,
 	Long pointSaveAmount ,
	Long couponDiscountAmount
) {
}
