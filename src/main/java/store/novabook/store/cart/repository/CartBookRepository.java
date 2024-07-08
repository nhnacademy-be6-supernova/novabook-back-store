package store.novabook.store.cart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.cart.entity.Cart;
import store.novabook.store.cart.entity.CartBook;

public interface CartBookRepository extends JpaRepository<CartBook, Long> {

	Optional<CartBook> findAllByCartId(Long cartId);

	Optional<CartBook> findByCartIdAndBookIdAndIsExposed(Long cartId, Long bookId, Boolean exposed);

	List<CartBook> findByCartIdAndBookIdInAndIsExposedTrue(Long cartId, List<Long> bookIds);

	Optional<CartBook> findByBookId(Long bookId);

	List<CartBook> findAllByCartAndBookIdInAndIsExposedTrue(Cart cart, List<Long> bookIds);

	CartBook findByCartAndBookIdAndIsExposedTrue(Cart cart, Long bookId);
}
