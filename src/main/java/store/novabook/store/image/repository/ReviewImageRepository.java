package store.novabook.store.image.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.image.entity.ReviewImage;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
	List<ReviewImage> findAllByReviewId(Long reviewId);
}
