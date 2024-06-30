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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.cart.dto.CreateCartBookRequest;
import store.novabook.store.cart.dto.DeleteCartBookRequest;
import store.novabook.store.cart.dto.GetCartBookListResponse;
import store.novabook.store.cart.dto.GetCartBookResponse;
import store.novabook.store.cart.service.CartBookService;

@Tag(name = "cart-book-controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/cart/books")
public class CartBookController {

	private final CartBookService cartBookService;

	@Operation(summary = "장바구니도서 페이징 조회", description = "장바구니도서 페이지를 조회합니다.")
	@Parameter(name = "cartId", description = "장바구니 ID", required = true)
	@GetMapping(value = "/{cartId}", params = {"page","size","order"} )
	public ResponseEntity<Page<GetCartBookResponse>> getAllCartBook(@PathVariable Long cartId, Pageable pageable) {
		Page<GetCartBookResponse> cartBookPage = cartBookService.getCartBookListByCartId(cartId, pageable);
		return ResponseEntity.status(HttpStatus.OK).body(cartBookPage);
	}

	@Operation(summary = "장바구니도서 조회", description = "장바구니도서를 조회합니다.")
	@GetMapping(value = "/{cartId}")
	public ResponseEntity<GetCartBookListResponse> getAllCartBook(@RequestHeader(required = false) Long memberId, @PathVariable Long cartId) {
		return ResponseEntity.ok().body(cartBookService.getCartBookListByCartId(cartId));
	}

	@Operation(summary = "장바구니도서 추가", description = "장바구니도서에 도서를 추가합니다.")
	@Parameter(name = "createCartBookRequest", description = "장바구니 도서 추가 정보", required = true)
	@PostMapping
	public ResponseEntity<Void> createCartBook(@Valid @RequestBody CreateCartBookRequest createCartBookRequest) {
		cartBookService.createCartBook(createCartBookRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Operation(summary = "장바구니 도서 삭제", description = "장바구니에 도서를 삭제합니다.")
	@Parameter(name = "deleteCartBookRequest", description = "장바구니 도서 삭제 정보", required = true)
	@DeleteMapping
	public ResponseEntity<Void> deleteCartBook(@Valid @RequestBody DeleteCartBookRequest deleteCartBookRequest) {
		cartBookService.deleteCartBookAndBook(deleteCartBookRequest);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
