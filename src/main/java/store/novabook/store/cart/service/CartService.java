package store.novabook.store.cart.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.cart.dto.CreateCartRequest;
import store.novabook.store.cart.dto.CreateCartResponse;
import store.novabook.store.cart.dto.GetCartResponse;
import store.novabook.store.cart.entity.Cart;
import store.novabook.store.cart.repository.CartRepository;
import store.novabook.store.common.exception.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
	private final CartRepository cartRepository;

	@Transactional(readOnly = true)
	public Page<CreateCartResponse> getCartList(Pageable pageable) {
		Page<Cart> cartList = cartRepository.findAll(pageable);

		return cartList.map(cart -> new CreateCartResponse(
			cart.getUsers(),
			cart.getIsExposed()
		));
	}

	public void createCart(CreateCartRequest createCartRequest) {
		cartRepository.save(
			Cart.builder().users(createCartRequest.users()).isExposed(createCartRequest.isExposed()).build());
	}

	public GetCartResponse getCartByUserId(Long userId) {
		Cart cart = cartRepository.findByUsersId(userId)
			.orElseThrow(() -> new EntityNotFoundException(Cart.class, userId));

		return new GetCartResponse(
			cart.getUsers(),
			cart.getIsExposed()
		);
	}
}
