package store.novabook.store.orders.service;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.orders.dto.CreateOrdersStatusRequest;
import store.novabook.store.orders.dto.CreateResponse;
import store.novabook.store.orders.dto.GetOrdersStatusResponse;
import store.novabook.store.orders.dto.UpdateOrdersStatusRequest;

public interface OrdersStatusService {
	CreateResponse save(CreateOrdersStatusRequest request);

	@Transactional(readOnly = true)
	Page<GetOrdersStatusResponse> getOrdersStatus();

	@Transactional(readOnly = true)
	GetOrdersStatusResponse getOrdersStatus(Long id);

	void updateOrdersStatus(Long id, UpdateOrdersStatusRequest request);
}
