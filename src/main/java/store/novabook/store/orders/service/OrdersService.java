package store.novabook.store.orders.service;

import org.springframework.data.domain.Page;

import store.novabook.store.orders.dto.request.CreateOrdersRequest;
import store.novabook.store.orders.dto.request.UpdateOrdersRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetOrdersResponse;

public interface OrdersService {
	//생성
	CreateResponse create(CreateOrdersRequest request);

	Page<GetOrdersResponse> getOrdersResponsesAll();

	GetOrdersResponse getOrdersById(Long id);

	void update(Long id, UpdateOrdersRequest request);
}
