package store.novabook.store.book.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.book.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	Page<Review> findByMemberId(Long memberId, Pageable pageable);

	Page<Review> findByBookId(Long bookId, Pageable pageable);

	boolean existsByMemberIdAndBookId(Long memberId, Long bookId);

}
