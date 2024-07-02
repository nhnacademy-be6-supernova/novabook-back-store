package store.novabook.store.cart.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.cart.dto.request.CreateCartRequest;
import store.novabook.store.cart.dto.response.GetCartResponse;
import store.novabook.store.cart.entity.Cart;
import store.novabook.store.cart.entity.CartBook;
import store.novabook.store.cart.repository.CartQueryRepository;
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
	private final CartQueryRepository queryRepository;

	@Override
	public void createCart(CreateCartRequest createCartRequest) {
		Member member = memberRepository.findById(createCartRequest.memberId())
			.orElseThrow(() -> new EntityNotFoundException(Cart.class, createCartRequest.memberId()));
	}

	@Override
	public GetCartResponse getCartByMemberId(Long memberId) {

		return queryRepository.getCartBook(memberId);
	}

}
