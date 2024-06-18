package store.novabook.store.cart.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.cart.dto.CreateCartRequest;
import store.novabook.store.cart.dto.CreateCartResponse;
import store.novabook.store.cart.dto.GetCartResponse;
import store.novabook.store.cart.service.CartService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

	private final CartService cartService;

	@GetMapping
	public ResponseEntity<Page<CreateCartResponse>> getAllCart(Pageable pageable) {

		Page<CreateCartResponse> cartList = cartService.getCartList(pageable);
		return ResponseEntity.status(HttpStatus.OK).body(cartList);
	}

	@PostMapping
	public ResponseEntity<Void> createCart(@ModelAttribute CreateCartRequest createCartRequest) {

		cartService.createCart(createCartRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/{userId}")
	public ResponseEntity<GetCartResponse> getCartByUserID(@PathVariable Long userId) {

		GetCartResponse getCartResponse = cartService.getCartByUserId(userId);
		return ResponseEntity.status(HttpStatus.OK).body(getCartResponse);
	}
}
