package store.novabook.store.book.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.novabook.store.book.dto.request.CreateReviewRequest;
import store.novabook.store.book.dto.request.UpdateReviewRequest;
import store.novabook.store.orders.entity.OrdersBook;

/**
 * 리뷰 정보를 저장하는 엔티티 클래스.
 * 주문과 책에 대한 리뷰를 관리한다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Review {

	/** 리뷰의 고유 ID. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 리뷰가 참조하는 주문 객체. */
	@NotNull
	@ManyToOne
	@JoinColumn(name = "orders_book_id")
	OrdersBook ordersBook;

	/** 리뷰 내용. */
	@NotNull
	private String content;

	/** 리뷰 점수. */
	@NotNull
	private int score;

	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	@Builder
	public Review(OrdersBook ordersBook, String content, int score) {
		this.ordersBook = ordersBook;
		this.content = content;
		this.score = score;
	}
	/**
	 * CreateReviewRequest 객체로부터 Review 엔티티를 생성.
	 * @param request 리뷰 생성 요청 데이터.
	 * @param ordersBook 관련 주문.
	 * @return 생성된 Review 객체.
	 */

	public static Review of(CreateReviewRequest request, OrdersBook ordersBook) {
		return Review.builder()
			.ordersBook(ordersBook)
			.content(request.content())
			.score(request.score())
			.build();
	}

	/**
	 * Review 엔티티를 주어진 UpdateReviewRequest 데이터로 업데이트.
	 * @param request 리뷰 업데이트 요청 데이터.
	 */
	public void update(UpdateReviewRequest request) {
		this.content = request.content();
		this.score = request.score();
	}
}
