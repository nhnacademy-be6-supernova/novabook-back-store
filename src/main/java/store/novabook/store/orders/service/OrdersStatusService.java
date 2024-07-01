package store.novabook.store.orders.service;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.orders.dto.request.CreateOrdersStatusRequest;
import store.novabook.store.orders.dto.request.UpdateOrdersStatusRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetOrdersStatusResponse;

public interface OrdersStatusService {
	CreateResponse save(CreateOrdersStatusRequest request);

	@Transactional(readOnly = true)
	Page<GetOrdersStatusResponse> getOrdersStatus();

	@Transactional(readOnly = true)
	GetOrdersStatusResponse getOrdersStatus(Long id);

	void updateOrdersStatus(Long id, UpdateOrdersStatusRequest request);
}
