package store.novabook.store.orders.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import store.novabook.store.book.dto.response.GetOrdersBookReviewIdResponse;
import store.novabook.store.orders.dto.request.CreateOrdersBookRequest;
import store.novabook.store.orders.dto.request.UpdateOrdersBookRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetOrdersBookResponse;

public interface OrdersBookService {
	CreateResponse create(CreateOrdersBookRequest request);

	Page<GetOrdersBookResponse> getOrdersBookAll();

	GetOrdersBookResponse getOrdersBook(Long id);

	void update(Long id, UpdateOrdersBookRequest request);

	void delete(Long id);

	Page<GetOrdersBookReviewIdResponse> getOrdersBookReviewByMemberId(Long memberId, Pageable pageable);
}
