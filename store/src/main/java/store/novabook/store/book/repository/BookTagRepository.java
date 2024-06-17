package store.novabook.store.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.book.entity.BookTag;

public interface BookTagRepository extends JpaRepository<BookTag, Long> {
}
