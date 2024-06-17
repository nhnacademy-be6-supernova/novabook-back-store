package store.novabook.store.cart.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.store.cart.repository.CartRepository;

@Service
@RequiredArgsConstructor
public class CartService {
	private final CartRepository cartRepository;
}
