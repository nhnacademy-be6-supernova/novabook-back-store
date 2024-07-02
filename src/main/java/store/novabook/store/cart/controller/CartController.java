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

import store.novabook.store.cart.dto.request.CreateCartRequest;
import store.novabook.store.cart.dto.response.GetCartResponse;
import store.novabook.store.cart.service.CartService;
import store.novabook.store.common.security.aop.CurrentUser;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/carts")
public class CartController implements CartControllerDocs {

	private final CartService cartService;

	@GetMapping
	public ResponseEntity<GetCartResponse> getCartByMemberID() {
		GetCartResponse getCartResponse = cartService.getCartByMemberId(3L);
		return ResponseEntity.status(HttpStatus.OK).body(getCartResponse);
	}

	@GetMapping("/list")
	public ResponseEntity<Void> createCart(@Valid @RequestBody CreateCartRequest createCartRequest) {
		cartService.createCart(createCartRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
