package store.novabook.store.book.repository.impl;

import static com.querydsl.jpa.JPAExpressions.*;
import static javax.management.Query.*;
import static store.novabook.store.book.entity.QReview.*;
import static store.novabook.store.image.entity.QImage.*;
import static store.novabook.store.image.entity.QReviewImage.*;
import static store.novabook.store.orders.entity.QOrdersBook.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;

import lombok.Getter;
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
	public List<GetReviewResponse> findReviewByBookId(Long bookId) {
		List<ReviewResult> reviews = from(review).select(
				Projections.constructor(ReviewResult.class, review.id, ordersBook.id, review.content, review.score, review.createdAt))
			.innerJoin(ordersBook).on(review.ordersBook.id.eq(ordersBook.id))
			.where(ordersBook.book.id.eq(bookId))
			.fetch();

		return reviews.stream()
			.map(r -> {
				List<String> images = select(image.source)
					.from(reviewImage)
					.innerJoin(image).on(reviewImage.image.id.eq(image.id))
					.where(reviewImage.review.id.eq(r.getReviewId()))
					.fetch();

				return GetReviewResponse.builder()
					.reviewId(r.getReviewId())
					.orderBookId(r.getOrderBookId())
					.content(r.getContent())
					.reviewImages(images)
					.createdAt(r.getCreatedAt())
					.score(r.getScore())
					.build();
			})
			.collect(Collectors.toList());
	}

	@Getter
	private static class ReviewResult {
		private final Long reviewId;
		private final Long orderBookId;
		private final String content;
		private final LocalDateTime createdAt;
		private final int score;

		public ReviewResult(Long reviewId, Long orderBookId, String content, LocalDateTime createdAt, int score) {
			this.reviewId = reviewId;
			this.orderBookId = orderBookId;
			this.content = content;
			this.createdAt = createdAt;
			this.score = score;
		}
	}
}
