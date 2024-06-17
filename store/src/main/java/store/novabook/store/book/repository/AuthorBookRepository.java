package store.novabook.store.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.book.entity.AuthorBook;

public interface AuthorBookRepository extends JpaRepository<AuthorBook, Long> {
}
