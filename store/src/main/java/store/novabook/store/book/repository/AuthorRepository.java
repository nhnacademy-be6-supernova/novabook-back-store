package store.novabook.store.book.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.book.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
