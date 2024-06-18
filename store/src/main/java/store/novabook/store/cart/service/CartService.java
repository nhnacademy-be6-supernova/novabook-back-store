package store.novabook.store.cart.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import store.novabook.store.cart.entity.Cart;
import store.novabook.store.cart.repository.CartRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
	private final CartRepository cartRepository;

	@GetMapping("/carts")
	public ResponseEntity<List<Cart>> getCarts(Pageable pageable) {
		Page<Cart> cartPage = cartRepository.findAll(pageable);
		return new ResponseEntity<>(cartPage.getContent(), HttpStatus.OK);
	}
}
