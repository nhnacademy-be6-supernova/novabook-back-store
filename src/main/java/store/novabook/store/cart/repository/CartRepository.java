package store.novabook.store.cart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.cart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
	Optional<Cart> findByMemberId(Long memberId);
	List<Cart> findByMemberIdAndIsExposedTrue(Long memberId);

}
