package store.novabook.store.orders.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import store.novabook.store.book.dto.response.GetOrdersBookReviewIdResponse;

public interface OrdersBookQueryRepository {
	Page<GetOrdersBookReviewIdResponse> getOrdersBookReviewIdByMemberId(Long memberId, Pageable pageable);
}
