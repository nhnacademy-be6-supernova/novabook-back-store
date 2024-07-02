package store.novabook.store.category.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.category.entity.BookCategory;

public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {
	boolean existsByCategoryId(Long categoryId);
	List<BookCategory> findAllByBookId(Long bookId);
}
