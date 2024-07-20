package store.novabook.store.orders.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record GetOrdersBookResponse(Long ordersId,
									String firstBookTitle,
									Long extraBookCount,
									Long totalAmount,
									String orderStatus,
									LocalDateTime createdAt) {
}


