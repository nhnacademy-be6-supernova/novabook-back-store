package store.novabook.store.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.cart.controller.docs.CartControllerDocs;
import store.novabook.store.cart.dto.CreateCartRequest;
import store.novabook.store.cart.dto.GetCartResponse;
import store.novabook.store.cart.service.CartService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/carts")
public class CartController implements CartControllerDocs {

	private final CartService cartService;

	@GetMapping("/{memberId}")
	public ResponseEntity<GetCartResponse> getCartByUserID(@PathVariable Long memberId) {
		GetCartResponse getCartResponse = cartService.getCartByMemberId(memberId);
		return ResponseEntity.status(HttpStatus.OK).body(getCartResponse);
	}

	@PostMapping
	public ResponseEntity<Void> createCart(@Valid @RequestBody CreateCartRequest createCartRequest) {
		cartService.createCart(createCartRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
