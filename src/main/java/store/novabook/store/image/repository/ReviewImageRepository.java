package store.novabook.store.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.image.entity.ReviewImage;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
}
