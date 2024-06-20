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
import store.novabook.store.cart.dto.CreateCartRequest;
import store.novabook.store.cart.dto.GetCartResponse;
import store.novabook.store.cart.service.CartService;
import store.novabook.store.common.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

	private final CartService cartService;

	// 이것도 만들고보니 쓸모가 없네요
	// @GetMapping
	// public ResponseEntity<Page<CreateCartResponse>> getAllCart(Pageable pageable) {
	//
	// 	Page<CreateCartResponse> cartList = cartService.getCartList(pageable);
	// 	return ResponseEntity.status(HttpStatus.OK).body(cartList);
	// }

	@GetMapping("/{userId}")
	public ApiResponse<GetCartResponse> getCartByUserID(@PathVariable Long userId) {

		GetCartResponse getCartResponse = cartService.getCartByUserId(userId);
		return new ApiResponse<>(getCartResponse);
	}

	@PostMapping
	public ResponseEntity<Void> createCart(@Valid @RequestBody CreateCartRequest createCartRequest) {

		cartService.createCart(createCartRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
