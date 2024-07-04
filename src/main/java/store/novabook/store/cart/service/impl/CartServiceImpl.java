package store.novabook.store.cart.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.cart.dto.response.CartIdResponse;
import store.novabook.store.cart.entity.Cart;
import store.novabook.store.cart.repository.CartRepository;
import store.novabook.store.cart.service.CartService;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

	private final CartRepository cartRepository;
	private final MemberRepository memberRepository;


	@Override
	public CartIdResponse createCartId(Long memberId){
		Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException(Member.class,memberId));
		Cart cart = cartRepository.save(Cart.of(member));
		return new CartIdResponse(cart.getId());
	}

	@Override
	@Transactional(readOnly = true)
	public CartIdResponse getCartIdByMemberId(Long memberId) {
		Optional<Cart> cart = cartRepository.findByMemberId(memberId);
		return cart.map(value -> new CartIdResponse(value.getId())).orElseGet(() -> new CartIdResponse(null));
	}

}
