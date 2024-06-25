package store.novabook.store.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.image.entity.BookImage;

public interface BookImageRepository extends JpaRepository<BookImage, Long> {
}
