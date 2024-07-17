package store.novabook.store.orders.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import store.novabook.store.orders.dto.request.UpdateOrdersAdminRequest;
import store.novabook.store.orders.dto.response.GetOrdersAdminResponse;
import store.novabook.store.orders.dto.response.GetOrdersResponse;

public interface OrdersService {

	Page<GetOrdersAdminResponse> getOrdersAdminResponsesAll(Pageable pageable);

	GetOrdersResponse getOrdersById(Long id);

	void update(Long id, UpdateOrdersAdminRequest request);
}
