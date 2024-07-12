package store.novabook.store.member.service;

import store.novabook.store.orders.dto.request.GetGuestOrderHistoryRequest;
import store.novabook.store.orders.dto.response.GetOrderDetailResponse;

public interface GuestService {

	GetOrderDetailResponse getOrderGuest(GetGuestOrderHistoryRequest request);
}
