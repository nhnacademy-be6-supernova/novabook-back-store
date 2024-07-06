package store.novabook.store.book.repository.impl;

import static store.novabook.store.book.entity.QReview.*;
import static store.novabook.store.image.entity.QImage.*;
import static store.novabook.store.image.entity.QReviewImage.*;
import static store.novabook.store.member.entity.QMember.*;
import static store.novabook.store.orders.entity.QOrders.*;
import static store.novabook.store.orders.entity.QOrdersBook.*;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;

import store.novabook.store.book.dto.ReviewImageDto;
import store.novabook.store.book.entity.Review;
import store.novabook.store.book.repository.ReviewQueryRepository;

@Repository
public class ReviewQueryRepositoryImpl extends QuerydslRepositorySupport implements ReviewQueryRepository {

	public ReviewQueryRepositoryImpl() {
		super(Review.class);
	}

	@Override
	public List<ReviewImageDto> findReviewByBookId(Long bookId) {
		return findReviewByCriteria(review.ordersBook.book.id.eq(bookId));
	}

	@Override
	public List<ReviewImageDto> findReviewByReviewId(Long reviewId) {
		return findReviewByCriteria(review.id.eq(reviewId));
	}

	private List<ReviewImageDto> findReviewByCriteria(BooleanExpression whereClause) {
		return from(review).select(
				Projections.constructor(ReviewImageDto.class, member.loginId, review.id,
					ordersBook.id, review.content, reviewImage.image.source, review.score,
					review.createdAt))
			.innerJoin(ordersBook).on(review.ordersBook.id.eq(ordersBook.id))
			.innerJoin(orders).on(ordersBook.orders.id.eq(orders.id))
			.innerJoin(member).on(orders.member.id.eq(member.id))
			.leftJoin(reviewImage).on(reviewImage.review.id.eq(review.id))
			.leftJoin(image).on(reviewImage.image.id.eq(image.id))
			.where(whereClause)
			.fetch();
	}
}
