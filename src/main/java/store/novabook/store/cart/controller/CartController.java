package store.novabook.store.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.cart.dto.request.CreateCartBookListRequest;
import store.novabook.store.cart.dto.request.CreateCartBookRequest;
import store.novabook.store.cart.dto.request.DeleteCartBookListRequest;
import store.novabook.store.cart.dto.request.UpdateCartBookQuantityRequest;
import store.novabook.store.cart.dto.response.CreateCartBookListResponse;
import store.novabook.store.cart.dto.response.CreateCartBookResponse;
import store.novabook.store.cart.dto.response.GetCartResponse;
import store.novabook.store.cart.service.CartBookService;
import store.novabook.store.cart.service.CartService;
import store.novabook.store.common.security.aop.CurrentMembers;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/carts")
public class CartController {

	private final CartService cartService;
	private final CartBookService cartBookService;

	//
	// @GetMapping
	// public ResponseEntity<CartIdResponse> getCartIdByMemberId(@CurrentMembers Long memberId) {
	// 	return ResponseEntity.ok().body(cartService.getCartIdByMemberId(memberId));
	// }
	// @PostMapping("/create")
	// public ResponseEntity<CartIdResponse> createCartIdByMemberId(@CurrentMembers Long memberId) {
	// 	return ResponseEntity.status(HttpStatus.CREATED).body(cartService.createCartId(memberId));
	// }

	// @GetMapping("/{cartId}")
	// public ResponseEntity<GetCartResponse> getCartListAll(@PathVariable Long cartId) {
	// 	GetCartResponse getCartResponse = cartBookService.getCartBookAll(cartId);
	// 	return ResponseEntity.status(HttpStatus.OK).body(getCartResponse);
	// }

	@GetMapping("/member")
	public ResponseEntity<GetCartResponse> getCartBookAllByMemberId(@CurrentMembers Long memberId) {
		return ResponseEntity.ok().body(cartBookService.getCartBookAllByMemberId(memberId));
	}

	@PostMapping("/add")
	public ResponseEntity<CreateCartBookResponse> addCartBook(@CurrentMembers Long memberId, @RequestBody CreateCartBookRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(cartBookService.createCartBook(memberId, request));
	}

	@PostMapping("/adds")
	public ResponseEntity<CreateCartBookListResponse> addCartBooks(@CurrentMembers Long memberId, @RequestBody CreateCartBookListRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(cartBookService.createCartBooks(memberId ,request));
	}

	@PutMapping("/update")
	public ResponseEntity<Void> updateCartBook(@CurrentMembers Long memberId, @RequestBody UpdateCartBookQuantityRequest request) {
		cartBookService.updateCartBookQuantity(memberId ,request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/{bookId}")
	public ResponseEntity<Void> deleteCartBook(@CurrentMembers Long memberId, @PathVariable Long bookId) {
		cartBookService.deleteCartBook(memberId, bookId);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteCartBooks(@CurrentMembers Long memberId, @RequestBody DeleteCartBookListRequest request) {
		cartBookService.deleteCartBooks(memberId, request);
		return ResponseEntity.ok().build();
	}

}
