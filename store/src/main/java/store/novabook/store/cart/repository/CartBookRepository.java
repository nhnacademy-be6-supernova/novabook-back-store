package store.novabook.store.cart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.cart.entity.CartBook;

public interface CartBookRepository extends JpaRepository<CartBook, Long> {

	Optional<List<CartBook>> findAllByCart_Id(Long cartId);

	Optional<CartBook> findByCart_IdAndBook_Id(Long cartId, Long bookId);

	void updateQuantityById(Long id, int quantity);
}
