package store.novabook.store.orders.service;

import org.springframework.data.domain.Page;

import store.novabook.store.orders.dto.CreateOrdersRequest;
import store.novabook.store.orders.dto.CreateResponse;
import store.novabook.store.orders.dto.GetOrdersResponse;
import store.novabook.store.orders.dto.UpdateOrdersRequest;

public interface OrdersService {
	//생성
	CreateResponse create(CreateOrdersRequest request);

	Page<GetOrdersResponse> getOrdersResponsesAll();

	GetOrdersResponse getOrdersById(Long id);

	void update(Long id, UpdateOrdersRequest request);
}
