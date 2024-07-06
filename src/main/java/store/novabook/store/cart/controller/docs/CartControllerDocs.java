package store.novabook.store.cart.controller.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import store.novabook.store.cart.dto.request.CreateCartBookRequest;
import store.novabook.store.cart.dto.response.CartIdResponse;
import store.novabook.store.cart.dto.response.CreateCartBookResponse;
import store.novabook.store.cart.dto.response.GetCartResponse;
import store.novabook.store.common.security.aop.CurrentMembers;

@Tag(name = "Cart API")
public interface CartControllerDocs {
	@Operation(summary = "장바구니 조회", description = "장바구니를 조회합니다.")
	@Parameter(name = "memberId", description = "회원 ID", required = true)
	ResponseEntity<CartIdResponse> getCartIdByMemberId(@CurrentMembers Long memberId);

	@Operation(summary = "장바구니 생성", description = "장바구니를 생성합니다.")
	@Parameter(name = "createCartRequest", description = "장바구니에 존재하는 MemberID를 포합니다.", required = true)
	ResponseEntity<CartIdResponse> createCartIdByMemberId(@CurrentMembers Long memberId);

	@Operation(summary = "장바구니 조회", description = "장바구니를 조회합니다.")
	ResponseEntity<GetCartResponse> getCartListAll(@PathVariable Long cartId);

	@Operation(summary = "장바구니 도서 추가", description = "장바구니에 도서를 추가합니다.")
	@Parameter(name = "CreateCartBookResponse", description = "CartId와 BookId, 수량을 포함힙니다.", required = true)
	ResponseEntity<CreateCartBookResponse> createCartBook(@RequestBody CreateCartBookRequest request);

	@Operation(summary = "장바구니 도서 삭제", description = "장바구니에 도서를 노출여부를 false로 변경합니다.")
	ResponseEntity<Void> deleteCartBook(@PathVariable Long cartId);
}
