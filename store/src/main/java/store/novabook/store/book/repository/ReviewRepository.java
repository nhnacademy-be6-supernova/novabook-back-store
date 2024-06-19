package store.novabook.store.book.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.book.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	List<Review> findByMemberId(Long memberId);
	Review findByMemberIdAndBookId(Long memberId, Long bookId);
	boolean existsByMemberIdAndBookId(Long memberId, Long bookId);
}
