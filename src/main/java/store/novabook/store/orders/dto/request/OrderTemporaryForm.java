package store.novabook.store.orders.dto.request;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
@RedisHash("OrderForm")
public record OrderTemporaryForm(
	@Id
	@NotNull
	Long memberId,
	@NotNull
	@Indexed
	String orderCode,
	@NotNull
	@Valid
	List<BookIdAndQuantityDTO> books,
	Long wrappingPaperId,
	Long couponId,
	@PositiveOrZero(message = "사용 포인트는 0보다 작을 수 없습니다.")
	long usePointAmount,
	@Future(message = "배송일은 주문일 이후로 가능합니다.")
	LocalDate deliveryDate,
	@NotNull(message = "배송비 ID는 null 일 수 없습니다.")
	@Indexed
	Long deliveryId,
	@Valid
	@NotNull
	OrderSenderInfo orderSenderInfo,
	@Valid
	@NotNull
	OrderReceiverInfo orderReceiverInfo
) {
}
