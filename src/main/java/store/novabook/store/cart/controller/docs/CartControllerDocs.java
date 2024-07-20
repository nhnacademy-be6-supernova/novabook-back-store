package store.novabook.store.cart.controller.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.cart.dto.CartBookDTO;
import store.novabook.store.cart.dto.CartBookIdDTO;
import store.novabook.store.cart.dto.CartBookListDTO;
import store.novabook.store.cart.dto.request.DeleteCartBookListRequest;
import store.novabook.store.cart.dto.request.UpdateCartBookQuantityRequest;
import store.novabook.store.cart.dto.response.CreateCartBookListResponse;
import store.novabook.store.cart.dto.response.CreateCartBookResponse;
import store.novabook.store.common.security.aop.CurrentMembers;

/**
 * 장바구니 관련 API 요청을 처리하는 컨트롤러 문서화 인터페이스입니다.
 */
@Tag(name = "Cart API")
public interface CartControllerDocs {

	/**
	 * 회원의 장바구니를 조회합니다.
	 *
	 * @param memberId 회원 ID
	 * @return 장바구니 도서 목록과 HTTP 상태 코드를 포함한 응답
	 */
	@Operation(summary = "장바구니 조회", description = "장바구니를 조회합니다.")
	@Parameter(description = "회원 ID", required = true)
	ResponseEntity<CartBookListDTO> getCartBookAllByMemberId(@CurrentMembers Long memberId);

	/**
	 * 게스트의 장바구니를 조회합니다.
	 *
	 * @param request 장바구니 조회를 위한 요청 데이터 (bookId 포함)
	 * @return 장바구니 도서 목록과 HTTP 상태 코드를 포함한 응답
	 */
	@Operation(summary = "장바구니 조회 (게스트)", description = "게스트의 장바구니를 조회합니다.")
	@Parameter(description = "장바구니 조회 요청 데이터 (bookId 포함)", required = true)
	ResponseEntity<CartBookListDTO> getCartBookAllByGuest(@Valid @RequestBody CartBookIdDTO request);

	/**
	 * 장바구니에 도서를 추가합니다.
	 *
	 * @param memberId 회원 ID (선택적)
	 * @param request 도서 추가 요청 데이터 (bookId와 수량 포함)
	 * @return 장바구니 도서 추가 응답과 HTTP 상태 코드를 포함한 응답
	 */
	@Operation(summary = "장바구니 도서 추가", description = "장바구니에 도서를 추가합니다.")
	@Parameter(description = "회원 ID (선택적)", required = false)
	@Parameter(description = "도서 추가 요청 데이터 (bookId와 수량 포함)", required = true)
	ResponseEntity<CreateCartBookResponse> addCartBook(@CurrentMembers(required = false) Long memberId,
		@RequestBody CartBookDTO request);

	/**
	 * 장바구니에 도서를 여러 개 추가합니다.
	 *
	 * @param memberId 회원 ID
	 * @param request 도서 추가 요청 데이터 (bookId와 수량 포함 리스트)
	 * @return 장바구니 도서 추가 응답과 HTTP 상태 코드를 포함한 응답
	 */
	@Operation(summary = "장바구니 도서 여러 개 추가", description = "장바구니에 도서를 여러 개 추가합니다.")
	@Parameter(description = "회원 ID", required = true)
	@Parameter(description = "도서 추가 요청 데이터 (bookId와 수량 포함 리스트)", required = true)
	ResponseEntity<CreateCartBookListResponse> addCartBooks(@CurrentMembers Long memberId,
		@RequestBody CartBookListDTO request);

	/**
	 * 장바구니에 도서 수량을 업데이트합니다.
	 *
	 * @param memberId 회원 ID
	 * @param request 도서 수량 업데이트 요청 데이터 (bookId와 수량 포함)
	 * @return HTTP 상태 코드
	 */
	@Operation(summary = "장바구니 도서 수량 업데이트", description = "장바구니에 도서 수량을 업데이트 합니다.")
	@Parameter(description = "회원 ID", required = true)
	@Parameter(description = "도서 수량 업데이트 요청 데이터 (bookId와 수량 포함)", required = true)
	ResponseEntity<Void> updateCartBook(@CurrentMembers Long memberId,
		@RequestBody UpdateCartBookQuantityRequest request);

	/**
	 * 장바구니에서 특정 도서를 삭제합니다.
	 *
	 * @param memberId 회원 ID
	 * @param bookId 도서 ID
	 * @return HTTP 상태 코드
	 */
	@Operation(summary = "장바구니 도서 삭제", description = "장바구니에서 특정 도서를 삭제합니다.")
	@Parameter(description = "회원 ID", required = true)
	@Parameter(description = "도서 ID", required = true)
	ResponseEntity<Void> deleteCartBook(@CurrentMembers Long memberId, @PathVariable Long bookId);

	/**
	 * 장바구니에서 여러 도서를 삭제합니다.
	 *
	 * @param memberId 회원 ID
	 * @param request 도서 삭제 요청 데이터 (bookId 리스트)
	 * @return HTTP 상태 코드
	 */
	@Operation(summary = "장바구니 도서 여러 개 삭제", description = "장바구니에서 여러 도서를 삭제합니다.")
	@Parameter(description = "회원 ID", required = true)
	@Parameter(description = "도서 삭제 요청 데이터 (bookId 리스트)", required = true)
	ResponseEntity<Void> deleteCartBooks(@CurrentMembers Long memberId, @RequestBody DeleteCartBookListRequest request);

}
