package store.novabook.store.orders.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import store.novabook.store.orders.dto.request.CreateOrdersRequest;
import store.novabook.store.orders.dto.request.UpdateOrdersAdminRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetOrdersAdminResponse;
import store.novabook.store.orders.dto.response.GetOrdersResponse;
import store.novabook.store.payment.entity.Payment;

public interface OrdersService {

	Page<GetOrdersResponse> getOrdersResponsesAll();

	CreateResponse create(CreateOrdersRequest request, Payment payment);

	Page<GetOrdersAdminResponse> getOrdersAdminResponsesAll(Pageable pageable);

	GetOrdersResponse getOrdersById(Long id);

	void update(Long id, UpdateOrdersAdminRequest request);
}
