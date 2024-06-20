package store.novabook.store.cart.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.cart.dto.CreateCartRequest;
import store.novabook.store.cart.dto.GetCartResponse;
import store.novabook.store.cart.entity.Cart;
import store.novabook.store.cart.repository.CartRepository;
import store.novabook.store.exception.EntityNotFoundException;
import store.novabook.store.user.member.entity.Users;
import store.novabook.store.user.member.repository.UsersRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
	private final CartRepository cartRepository;

	private final UsersRepository usersRepository;

	public void createCart(CreateCartRequest createCartRequest) {

		Users users = usersRepository.findById(createCartRequest.userId())
			.orElseThrow(() -> new EntityNotFoundException(Cart.class, createCartRequest.userId()));

		cartRepository.save(
			Cart.builder()
				.users(users)
				.isExposed(createCartRequest.isExposed())
				.createdAt(LocalDateTime.now())
				.build());
	}

	// 이건 필요한지 필요없는지 모르겠어서 그냥 주석만 해둘게요(무지성으로 만들어버림..)
	// @Transactional(readOnly = true)
	// public Page<CreateCartResponse> getCartList(Pageable pageable) {
	// 	Page<Cart> cartList = cartRepository.findAll(pageable);
	//
	// 	return cartList.map(cart -> new CreateCartResponse(
	// 		cart.getUsers(),
	// 		cart.getIsExposed()
	// 	));
	// }

	@Transactional(readOnly = true)
	public GetCartResponse getCartByUserId(Long userId) {
		Cart cart = cartRepository.findByUsersId(userId)
			.orElseThrow(() -> new EntityNotFoundException(Cart.class, userId));

		return new GetCartResponse(
			cart.getUsers(),
			cart.getIsExposed()
		);
	}

}
