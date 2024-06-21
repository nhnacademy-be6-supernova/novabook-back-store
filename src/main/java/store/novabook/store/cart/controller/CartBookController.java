package store.novabook.store.cart.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import store.novabook.store.cart.dto.CreateCartBookRequest;
import store.novabook.store.cart.entity.CartBook;
import store.novabook.store.cart.service.CartBookService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart/books")
public class CartBookController {

	private final CartBookService cartBookService;

	@GetMapping("/{cartId}")
	public ResponseEntity<Page<CartBook>> getAllCartBook(@PathVariable() Long cartId, Pageable pageable) {
		// List<CartBook> cartBookListByCartId = cartBookService.getCartBookListByCartId(cartId);
		Page<CartBook> cartBookPage = cartBookService.getCartBookListByCartId(cartId, pageable);
		return ResponseEntity.status(HttpStatus.OK).body(cartBookPage);
	}

	@PostMapping
	public ResponseEntity<Void> createCartBook(@Valid @RequestBody CreateCartBookRequest createCartBookRequest) {
		cartBookService.createCartBook(createCartBookRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
