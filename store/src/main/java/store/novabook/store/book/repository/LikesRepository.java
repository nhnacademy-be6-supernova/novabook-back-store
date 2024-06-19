package store.novabook.store.book.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.validation.constraints.NotNull;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.Likes;
import store.novabook.store.user.member.entity.Member;

public interface LikesRepository extends JpaRepository<Likes, Long> {
	int countByBookId(Long bookId);
	List<Likes> findAllByMemberId(Long memberId);
	boolean existsByMemberIdAndBookId(Long memberId, Long bookId);
	Likes findByMemberIdAndBookId(Long memberId, Long bookId);
}
