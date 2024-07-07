package store.novabook.store.book.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.book.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewQueryRepository {
	Page<Review> findByOrdersBookId(Long ordersId, Pageable pageable);

	boolean existsByOrdersBookId(Long ordersBookId);

	Page<Review> findAllByOrdersBookOrdersMemberId(Long memberId, Pageable pageable);

}
