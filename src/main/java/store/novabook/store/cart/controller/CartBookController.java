package store.novabook.store.cart.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.cart.controller.docs.CartBookControllerDocs;
import store.novabook.store.cart.dto.request.CreateCartBookRequest;
import store.novabook.store.cart.dto.request.DeleteCartBookRequest;
import store.novabook.store.cart.dto.response.GetCartBookResponse;
import store.novabook.store.cart.service.CartBookService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/cart/books")
public class CartBookController implements CartBookControllerDocs {

	private final CartBookService cartBookService;

	@GetMapping("/{cartId}")
	public ResponseEntity<Page<GetCartBookResponse>> getAllCartBook(@PathVariable Long cartId, Pageable pageable) {
		Page<GetCartBookResponse> cartBookPage = cartBookService.getCartBookListByCartId(cartId, pageable);
		return ResponseEntity.status(HttpStatus.OK).body(cartBookPage);
	}

	@GetMapping
	public ResponseEntity<GetCartBookResponse> getCartId() {


		return ResponseEntity.ok().build();

	}

	@PostMapping
	public ResponseEntity<Void> createCartBook(@Valid @RequestBody CreateCartBookRequest createCartBookRequest) {
		cartBookService.createCartBook(createCartBookRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteCartBook(@Valid @RequestBody DeleteCartBookRequest deleteCartBookRequest) {
		cartBookService.deleteCartBookAndBook(deleteCartBookRequest);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
