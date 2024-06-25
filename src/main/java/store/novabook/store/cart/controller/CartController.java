package store.novabook.store.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.cart.dto.CreateCartRequest;
import store.novabook.store.cart.dto.GetCartResponse;
import store.novabook.store.cart.service.CartService;

@Tag(name = "cart-controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/carts")
public class CartController {

	private final CartService cartService;

	// 이것도 만들고보니 쓸모가 없네요
	// @GetMapping
	// public ResponseEntity<Page<CreateCartResponse>> getAllCart(Pageable pageable) {
	//
	// 	Page<CreateCartResponse> cartList = cartService.getCartList(pageable);
	// 	return ResponseEntity.status(HttpStatus.OK).body(cartList);
	// }

	@Operation(summary = "장바구니 조회", description = "장바구니를 조회합니다.")
	@Parameter(name = "memberId", description = "사용자 ID", required = true)
	@GetMapping("/{memberId}")
	public ResponseEntity<GetCartResponse> getCartByUserID(@PathVariable Long memberId) {

		GetCartResponse getCartResponse = cartService.getCartByMemberId(memberId);
		return ResponseEntity.status(HttpStatus.OK).body(getCartResponse);
	}

	@Operation(summary = "장바구니 생성", description = "장바구니를 생성합니다.")
	@Parameter(name = "createCartRequest", description = "장바구니 생성 정보", required = true)
	@PostMapping
	public ResponseEntity<Void> createCart(@Valid @RequestBody CreateCartRequest createCartRequest) {

		cartService.createCart(createCartRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
