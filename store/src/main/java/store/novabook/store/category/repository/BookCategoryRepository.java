package store.novabook.store.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.category.entity.BookCategory;

public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {
}
