package store.novabook.store.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.book.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}
