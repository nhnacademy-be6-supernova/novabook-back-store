package store.novabook.store.member.service;

import store.novabook.store.orders.dto.request.GetGuestOrderHistoryRequest;
import store.novabook.store.orders.dto.response.GetOrdersResponse;

public interface GuestService {

	GetOrdersResponse getOrderGuest(GetGuestOrderHistoryRequest request);
}
