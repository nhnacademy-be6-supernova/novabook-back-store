package store.novabook.store.orders.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import store.novabook.store.orders.dto.request.CreateOrdersRequest;
import store.novabook.store.orders.dto.request.UpdateOrdersAdminRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetOrdersAdminResponse;
import store.novabook.store.orders.dto.response.GetOrdersResponse;

public interface OrdersService {
	//생성
	CreateResponse create(CreateOrdersRequest request);

	Page<GetOrdersResponse> getOrdersResponsesAll();

	Page<GetOrdersAdminResponse> getOrdersAdminResponsesAll(Pageable pageable);

	GetOrdersResponse getOrdersById(Long id);

	void update(Long id, UpdateOrdersAdminRequest request);
}
