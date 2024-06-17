package store.novabook.store.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.cart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
