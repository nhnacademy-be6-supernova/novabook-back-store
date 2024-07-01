package store.novabook.store.orders.dto.response;

import java.util.List;

import lombok.Builder;

@Builder
public record GetDeliveryFeeListResponse(
	List<GetDeliveryFeeResponse> getDeliveryFeeResponses) {
}
