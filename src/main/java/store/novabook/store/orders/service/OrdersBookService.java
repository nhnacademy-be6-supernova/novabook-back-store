package store.novabook.store.orders.service;

import org.springframework.data.domain.Page;

import store.novabook.store.orders.dto.CreateOrdersBookRequest;
import store.novabook.store.orders.dto.CreateResponse;
import store.novabook.store.orders.dto.GetOrdersBookResponse;
import store.novabook.store.orders.dto.UpdateOrdersBookRequest;

public interface OrdersBookService {
	CreateResponse create(CreateOrdersBookRequest request);

	Page<GetOrdersBookResponse> getOrdersBookAll();

	GetOrdersBookResponse getOrdersBook(Long id);

	void update(Long id, UpdateOrdersBookRequest request);

	void delete(Long id);
}
