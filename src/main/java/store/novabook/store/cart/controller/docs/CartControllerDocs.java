package store.novabook.store.cart.controller.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import store.novabook.store.cart.dto.CartBookDTO;
import store.novabook.store.cart.dto.CartBookListDTO;
import store.novabook.store.cart.dto.request.DeleteCartBookListRequest;
import store.novabook.store.cart.dto.request.UpdateCartBookQuantityRequest;
import store.novabook.store.cart.dto.response.CreateCartBookListResponse;
import store.novabook.store.cart.dto.response.CreateCartBookResponse;
import store.novabook.store.common.security.aop.CurrentMembers;

@Tag(name = "Cart API")
public interface CartControllerDocs {

	@Operation(summary = "장바구니 조회", description = "장바구니를 조회합니다.")
	@Parameter(name = "memberId", description = "회원 ID", required = true)
	@GetMapping("/member")
	ResponseEntity<CartBookListDTO> getCartBookAllByMemberId(@CurrentMembers Long memberId);

	@Operation(summary = "장바구니 도서 추가", description = "장바구니에 도서를 추가합니다.")
	@Parameter(name = "memberId", description = "회원 ID", required = true)
	@Parameter(name = "createCartRequest", description = "bookId와 수량을 포함합니다.", required = true)
	@PostMapping("/add")
	ResponseEntity<CreateCartBookResponse> addCartBook(@CurrentMembers Long memberId, @RequestBody CartBookDTO request);

	@Operation(summary = "장바구니 도서여러개 추가", description = "장바구니에 도서를 여러개 추가합니다.")
	@Parameter(name = "memberId", description = "회원 ID", required = true)
	@Parameter(name = "CreateCartBookListRequest", description = "bookId와 수량을 포함한 List 입니다.", required = true)
	@PostMapping("/adds")
	ResponseEntity<CreateCartBookListResponse> addCartBooks(@CurrentMembers Long memberId,
		@RequestBody CartBookListDTO request);

	@Operation(summary = "장바구니 도서 업데이트", description = "장바구니에 도서 수량을 업데이트 합니다.")
	@Parameter(name = "memberId", description = "회원 ID", required = true)
	@Parameter(name = "UpdateCartBookQuantityRequest", description = "bookId와 수량을 포함합니다.", required = true)
	@PutMapping("/update")
	ResponseEntity<Void> updateCartBook(@CurrentMembers Long memberId,
		@RequestBody UpdateCartBookQuantityRequest request);

	@Operation(summary = "장바구니 도서 삭제", description = "장바구니에 도서를 노출여부를 false로 변경합니다.")
	@Parameter(name = "memberId", description = "회원 ID", required = true)
	@Parameter(name = "bookId", description = "bookId", required = true)
	@DeleteMapping("/{bookId}")
	ResponseEntity<Void> deleteCartBook(@CurrentMembers Long memberId, @PathVariable Long bookId);

	@Operation(summary = "장바구니 도서 삭제", description = "장바구니에 도서를 노출여부를 false로 변경합니다.")
	@Parameter(name = "memberId", description = "회원 ID", required = true)
	@Parameter(name = "DeleteCartBookListRequest", description = "bookId List", required = true)
	@DeleteMapping
	ResponseEntity<Void> deleteCartBooks(@CurrentMembers Long memberId, @RequestBody DeleteCartBookListRequest request);

}
