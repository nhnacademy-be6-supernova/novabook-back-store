package store.novabook.store.cart.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.cart.dto.CreateCartRequest;
import store.novabook.store.cart.dto.GetCartResponse;
import store.novabook.store.cart.entity.Cart;
import store.novabook.store.cart.repository.CartRepository;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
	private final CartRepository cartRepository;

	private final MemberRepository memberRepository;

	public void createCart(CreateCartRequest createCartRequest) {

		Member member = memberRepository.findById(createCartRequest.memberId())
			.orElseThrow(() -> new EntityNotFoundException(Cart.class, createCartRequest.memberId()));

		cartRepository.save(
			Cart.builder()
				.member(member)
				.isExposed(createCartRequest.isExposed())
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
	public GetCartResponse getCartByMemberId(Long memberId) {
		Cart cart = cartRepository.findByMemberId(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Cart.class, memberId));

		return new GetCartResponse(
			cart.getMember(),
			cart.getIsExposed()
		);
	}

}
