package store.novabook.store.orders.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import store.novabook.store.book.dto.response.GetOrdersBookReviewIdResponse;

public interface OrdersBookQueryRepository {
	//주문한 책의 주문아이디 타이틀, 책아이디, 주문일, 리뷰 아이디(null가능)  줌
	Page<GetOrdersBookReviewIdResponse> getOrdersBookReviewIdByMemberId(Long memberId, Pageable pageable);
}
