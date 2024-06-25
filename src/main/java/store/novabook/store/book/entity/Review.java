package store.novabook.store.book.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.novabook.store.book.dto.CreateReviewRequest;
import store.novabook.store.book.dto.UpdateReviewRequest;
import store.novabook.store.orders.entity.Orders;
import store.novabook.store.user.member.entity.Member;

/**
 * 리뷰 정보를 저장하는 엔티티 클래스.
 * 주문과 책에 대한 리뷰를 관리한다.
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Review {

	/** 리뷰의 고유 ID. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 리뷰가 참조하는 책 객체. */
	@NotNull
	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;

	/** 리뷰가 참조하는 주문 객체. */
	@NotNull
	@ManyToOne
	@JoinColumn(name = "order_id")
	Orders orders;

	/** 리뷰 내용. */
	@NotNull
	private String content;

	/** 리뷰 점수. */
	@NotNull
	private int score;

	/** 리뷰 생성 일시. */
	@NotNull
	private LocalDateTime createdAt;

	/** 리뷰 수정 일시. */
	private LocalDateTime updatedAt;

	/**
	 * CreateReviewRequest 객체로부터 Review 엔티티를 생성.
	 * @param request 리뷰 생성 요청 데이터.
	 * @param orders 관련 주문.
	 * @param book 관련 책.
	 * @return 생성된 Review 객체.
	 */
	public static Review toEntity(CreateReviewRequest request, Orders orders, Book book) {
		Review review = new Review();
		review.book = book;
		review.orders = orders;
		review.content = request.content();
		review.score = request.score();
		review.createdAt = LocalDateTime.now();
		review.updatedAt = null;
		return review;
	}

	/**
	 * UpdateReviewRequest 객체로부터 Review 엔티티를 업데이트.
	 * @param request 리뷰 업데이트 요청 데이터.
	 * @param orders 관련 주문.
	 * @param book 관련 책.
	 * @return 업데이트된 Review 객체.
	 */
	public static Review toEntity(UpdateReviewRequest request, Orders orders, Book book) {
		Review review = new Review();
		review.orders = orders;
		review.book = book;
		review.content = request.content();
		review.score = request.score();
		review.updatedAt = LocalDateTime.now();
		return review;
	}

	/**
	 * Review 엔티티를 주어진 UpdateReviewRequest 데이터로 업데이트.
	 * @param request 리뷰 업데이트 요청 데이터.
	 */
	public void updateEntity(UpdateReviewRequest request) {
		this.updatedAt = LocalDateTime.now();
		this.content = request.content();
		this.score = request.score();
	}
}
