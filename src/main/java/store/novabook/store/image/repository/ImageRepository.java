package store.novabook.store.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.image.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
