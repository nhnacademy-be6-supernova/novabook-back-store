package store.novabook.store.cart.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.cart.entity.CartBook;

public interface CartBookRepository extends JpaRepository<CartBook, Long> {

	Optional<Page<CartBook>> findAllByCartId(Long cartId, Pageable pageable);

	Optional<CartBook> findByCartIdAndBookId(Long cartId, Long bookId);

	boolean existsByCartIdAndBookId(Long cartId, Long bookId);

	void deleteByCartIdAndBookId(Long cartId, Long bookId);
}
