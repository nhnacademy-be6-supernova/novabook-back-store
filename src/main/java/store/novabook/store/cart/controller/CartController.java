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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.cart.controller.docs.CartControllerDocs;
import store.novabook.store.cart.dto.CartBookDTO;
import store.novabook.store.cart.dto.CartBookIdDTO;
import store.novabook.store.cart.dto.request.CreateCartBookListRequest;
import store.novabook.store.cart.dto.request.CreateCartBookRequest;
import store.novabook.store.cart.dto.request.DeleteCartBookListRequest;
import store.novabook.store.cart.dto.request.UpdateCartBookQuantityRequest;
import store.novabook.store.cart.dto.CartBookListDTO;
import store.novabook.store.cart.dto.response.CreateCartBookListResponse;
import store.novabook.store.cart.dto.response.CreateCartBookResponse;

import store.novabook.store.cart.service.CartBookService;
import store.novabook.store.common.security.aop.CurrentMembers;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/carts")
public class CartController implements CartControllerDocs {

	private final CartBookService cartBookService;

	@GetMapping("/member")
	public ResponseEntity<CartBookListDTO> getCartBookAllByMemberId(@CurrentMembers Long memberId) {
		return ResponseEntity.ok().body(cartBookService.getCartBookAllByMemberId(memberId));
	}

	@PostMapping("/guest")
	public ResponseEntity<CartBookListDTO> getCartBookAllByGuest(@Valid @RequestBody CartBookIdDTO request) {
		return ResponseEntity.ok().body(cartBookService.getCartBookAllByGuest(request));
	}

	@PostMapping("/add")
	public ResponseEntity<CreateCartBookResponse> addCartBook(@CurrentMembers Long memberId, @Valid @RequestBody CartBookDTO request) {
		return ResponseEntity.status(HttpStatus.OK).body(cartBookService.createCartBook(memberId, request));
	}

	@PostMapping("/adds")
	public ResponseEntity<CreateCartBookListResponse> addCartBooks(@CurrentMembers Long memberId, @Valid  @RequestBody CartBookListDTO request) {
		return ResponseEntity.status(HttpStatus.OK).body(cartBookService.createCartBooks(memberId ,request));
	}

	@PutMapping("/update")
	public ResponseEntity<Void> updateCartBook(@CurrentMembers Long memberId, @Valid @RequestBody UpdateCartBookQuantityRequest request) {
		cartBookService.updateCartBookQuantity(memberId ,request);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/{bookId}")
	public ResponseEntity<Void> deleteCartBook(@CurrentMembers Long memberId, @PathVariable Long bookId) {
		cartBookService.deleteCartBook(memberId, bookId);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteCartBooks(@CurrentMembers Long memberId, @Valid @RequestBody DeleteCartBookListRequest request) {
		cartBookService.deleteCartBooks(memberId, request);
		return ResponseEntity.ok().build();
	}

}
