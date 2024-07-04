package store.novabook.store.book.repository.impl;

import static store.novabook.store.book.entity.QReview.*;
import static store.novabook.store.image.entity.QReviewImage.*;
import static store.novabook.store.member.entity.QMember.*;
import static store.novabook.store.orders.entity.QOrders.*;
import static store.novabook.store.orders.entity.QOrdersBook.*;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;

import store.novabook.store.book.dto.ReviewImageDto;
import store.novabook.store.book.dto.response.GetReviewResponse;
import store.novabook.store.book.entity.Review;
import store.novabook.store.book.repository.ReviewQueryRepository;

@Repository
@Transactional(readOnly = true)
public class ReviewQueryRepositoryImpl extends QuerydslRepositorySupport implements ReviewQueryRepository {

	public ReviewQueryRepositoryImpl() {
		super(Review.class);
	}

	@Override
	public List<ReviewImageDto> findReviewByBookId(Long bookId) {
		return from(review).select(
				Projections.constructor(ReviewImageDto.class, member.loginId, review.id,
					ordersBook.id, review.content, reviewImage.image.source, reviewImage.review.score,
					reviewImage.review.createdAt))
			.innerJoin(reviewImage).on(reviewImage.review.id.eq(review.id))
			.innerJoin(ordersBook).on(review.ordersBook.id.eq(ordersBook.id))
			.innerJoin(orders).on(ordersBook.orders.id.eq(orders.id))
			.innerJoin(member).on(orders.member.id.eq(member.id))

			.where(review.ordersBook.book.id.eq(bookId))
			.fetch();
	}
}
