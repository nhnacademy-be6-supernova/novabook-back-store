package store.novabook.store.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import store.novabook.store.book.entity.BookStatus;

public interface BookStatusRepository extends JpaRepository<BookStatus, Long> {
}
