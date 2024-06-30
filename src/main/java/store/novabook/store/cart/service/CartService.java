package store.novabook.store.cart.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import store.novabook.store.cart.dto.CreateCartRequest;
import store.novabook.store.cart.dto.GetCartResponse;
import store.novabook.store.cart.entity.Cart;
import store.novabook.store.cart.repository.CartRepository;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.user.member.entity.Member;
import store.novabook.store.user.member.repository.MemberRepository;

@Slf4j
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

	@Transactional(readOnly = true)
	public GetCartResponse getCartByMemberId(Long memberId, boolean isExposed) {
		List<Long> cartIds = new ArrayList<>();

		if(isExposed) {
			List<Cart> cartList = cartRepository.findByMemberIdAndIsExposedTrue(memberId);
			for (Cart cart : cartList) {
				cartIds.add(cart.getId());
			}
		} else {
			log.error("노출여부가 false인 기능은 구현하지 않았습니다.");
		}

		return GetCartResponse.builder().cartId(cartIds).build();
	}
}
