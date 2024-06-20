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
import store.novabook.store.user.member.entity.Member;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;

	@NotNull
	private String content;

	private String image;

	@NotNull
	private int score;

	@NotNull
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	public static Review toEntity(CreateReviewRequest request, Member member, Book book) {
		Review review = new Review();
		review.member = member;
		review.book = book;
		review.content = request.content();
		review.image = request.image();
		review.score = request.score();
		review.createdAt = LocalDateTime.now();
		review.updatedAt = null;  // 생성 시점에는 업데이트 시간이 없으므로 null로 설정

		return review;
	}
	public static Review toEntity(UpdateReviewRequest request, Member member, Book book) {
		Review review = new Review();
		review.member = member;
		review.book = book;
		review.content = request.content();
		review.image = request.image();
		review.score = request.score();
		review.updatedAt = LocalDateTime.now();  // 생성 시점에는 업데이트 시간이 없으므로 null로 설정

		return review;
	}
	public void updateEntity(UpdateReviewRequest request) {
		this.updatedAt = LocalDateTime.now();
		this.content = request.content();
		this.image = request.image();
		this.score = request.score();
	}
}
