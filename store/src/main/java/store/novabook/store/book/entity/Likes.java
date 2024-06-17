package store.novabook.store.book.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.novabook.store.user.member.entity.Member;
import store.novabook.store.user.member.entity.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Likes {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

	@NotNull
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}
