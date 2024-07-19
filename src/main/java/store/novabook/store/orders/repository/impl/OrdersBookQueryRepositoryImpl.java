package store.novabook.store.orders.repository.impl;

import static store.novabook.store.book.entity.QBook.*;
import static store.novabook.store.book.entity.QReview.*;
import static store.novabook.store.orders.entity.QOrders.*;
import static store.novabook.store.orders.entity.QOrdersBook.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;

import store.novabook.store.book.dto.response.GetOrdersBookReviewIdResponse;
import store.novabook.store.orders.dto.response.GetOrdersBookResponse;
import store.novabook.store.orders.entity.OrdersBook;
import store.novabook.store.orders.entity.QOrdersBook;
import store.novabook.store.orders.repository.OrdersBookQueryRepository;

@Repository
@Transactional(readOnly = true)
public class OrdersBookQueryRepositoryImpl extends QuerydslRepositorySupport implements OrdersBookQueryRepository {

	/* TODO query 생성자 일단 지워놨으니까 나중에 관련해서 배포하고 돌려보고 안되면 브랜치 비교해서 삭제 or 잘되면 주석 삭제해주세요 */
	public OrdersBookQueryRepositoryImpl() {
		super(OrdersBook.class);
	}

	public Page<GetOrdersBookReviewIdResponse> getOrdersBookReviewIdByMemberId(Long memberId, Pageable pageable) {
		List<GetOrdersBookReviewIdResponse> responses = from(ordersBook).select(
				Projections.constructor(GetOrdersBookReviewIdResponse.class, ordersBook.id, review.id, orders.id, book.id,
					book.title, orders.createdAt))
			.innerJoin(book)
			.on(ordersBook.book.id.eq(book.id))
			.innerJoin(orders)
			.on(orders.id.eq(ordersBook.orders.id))
			.leftJoin(review)
			.on(ordersBook.id.eq(review.ordersBook.id))
			.where(orders.member.id.eq(memberId))
			.fetch();
		return new PageImpl<>(responses, pageable, responses.size());
	}

	@Override
	public Page<GetOrdersBookResponse> getOrdersBookByMemberId(Long memberId, Pageable pageable) {
		QOrdersBook ordersBook = QOrdersBook.ordersBook;

		com.querydsl.jpa.JPQLQuery<GetOrdersBookResponse> query = from(ordersBook).select(
				Projections.constructor(GetOrdersBookResponse.class, ordersBook.orders.id.as("ordersId"),
					ordersBook.book.title.min().as("firstBookTitle"),
					ordersBook.book.count().subtract(1).as("extraBookCount"),
					ordersBook.orders.totalAmount.max().as("totalAmount"),
					ordersBook.orders.ordersStatus.name.as("orderStatus"), ordersBook.createdAt.min().as("createdAt")))
			.join(ordersBook.book)
			.join(ordersBook.orders)
			.join(ordersBook.orders.ordersStatus)
			.where(ordersBook.orders.member.id.eq(memberId))
			.groupBy(ordersBook.orders.id, ordersBook.orders.totalAmount, ordersBook.orders.ordersStatus.name);

		// 페이징 처리
		Querydsl querydsl = getQuerydsl();
		if (querydsl == null) {
			throw new IllegalArgumentException("Querydsl is null");
		}
		List<GetOrdersBookResponse> results = querydsl.applyPagination(pageable, query).fetch();
		long totalCount = query.fetchCount();

		return new PageImpl<>(results, pageable, totalCount);
	}

	@Override
	public Page<GetOrdersBookResponse> getOrdersBookAllByMemberId(Long memberId, Pageable pageable) {
		return null;
	}
}
