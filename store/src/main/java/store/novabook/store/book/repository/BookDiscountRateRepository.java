package store.novabook.store.book.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.book.entity.BookDiscountRate;

public interface BookDiscountRateRepository extends JpaRepository<BookDiscountRate, Long> {
	BookDiscountRate findByBookId(Long bookId);
}
