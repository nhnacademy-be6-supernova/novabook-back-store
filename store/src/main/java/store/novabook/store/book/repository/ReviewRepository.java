package store.novabook.store.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.book.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
