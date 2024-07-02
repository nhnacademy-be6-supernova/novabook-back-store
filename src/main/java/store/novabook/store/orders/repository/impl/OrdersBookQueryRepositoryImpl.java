package store.novabook.store.orders.repository.impl;

import static store.novabook.store.book.entity.QBook.*;
import static store.novabook.store.book.entity.QReview.*;
import static store.novabook.store.orders.entity.QOrders.*;
import static store.novabook.store.orders.entity.QOrdersBook.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;

import store.novabook.store.book.dto.response.GetOrdersBookReviewIdResponse;
import store.novabook.store.orders.entity.OrdersBook;
import store.novabook.store.orders.repository.OrdersBookQueryRepository;
@Repository
@Transactional(readOnly = true)
public class OrdersBookQueryRepositoryImpl extends QuerydslRepositorySupport implements OrdersBookQueryRepository {
	public OrdersBookQueryRepositoryImpl() {
		super(OrdersBook.class);
	}

	public Page<GetOrdersBookReviewIdResponse> getOrdersBookReviewIdByMemberId(Long memberId, Pageable pageable) {
		List<GetOrdersBookReviewIdResponse> responses = from(ordersBook).select(
				Projections.constructor(GetOrdersBookReviewIdResponse.class, ordersBook.id, review.id, orders.id, book.id, book.title,
					orders.createdAt))
			.innerJoin(book).on(ordersBook.book.id.eq(book.id))
			.innerJoin(orders).on(orders.id.eq(ordersBook.orders.id))
			.leftJoin(review).on(ordersBook.id.eq(review.ordersBook.id))
			.where(orders.member.id.eq(memberId))
			.fetch();
		return new PageImpl<>(responses, pageable, responses.size());
	}
}
