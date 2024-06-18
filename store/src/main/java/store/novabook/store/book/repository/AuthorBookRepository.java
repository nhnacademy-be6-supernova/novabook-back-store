package store.novabook.store.book.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.book.entity.AuthorBook;

public interface AuthorBookRepository extends JpaRepository<AuthorBook, Long> {
	List<AuthorBook> findByAuthorId(Long authorId);

	List<AuthorBook> findByBookId(Long bookId);
}
