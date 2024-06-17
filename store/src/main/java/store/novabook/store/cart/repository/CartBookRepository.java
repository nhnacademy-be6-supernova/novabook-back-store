package store.novabook.store.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.cart.entity.CartBook;

public interface CartBookRepository extends JpaRepository<CartBook, Long> {
}
