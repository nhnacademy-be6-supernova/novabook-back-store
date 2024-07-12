package store.novabook.store.orders.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.book.dto.response.GetOrdersBookReviewIdResponse;
import store.novabook.store.common.security.aop.CurrentMembers;
import store.novabook.store.orders.dto.request.CreateOrdersBookRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetOrdersBookResponse;

@Tag(name = "OrdersBook API", description = "주문-도서 관계테이블 API")
public interface OrdersBookControllerDocs {

	/**
	 * <주문도서>를 생성하는 API입니다.
	 *
	 * @param request 주문도서 생성 요청 객체
	 * @return 생성된 주문도서의 ID를 포함한 응답 객체
	 */
	@Operation(summary = "<주문도서> 생성", description = "<주문도서>를 생성합니다 ")
	ResponseEntity<CreateResponse> createOrdersBook(@Valid @RequestBody CreateOrdersBookRequest request);

	/**
	 * <주문도서>를 전체 조회하는 API입니다.
	 *
	 * @param memberId 회원 ID
	 * @param pageable 페이지 요청 객체
	 * @return 페이지로 분류된 주문도서 응답 객체
	 */
	@Operation(summary = "<주문도서> 전체 조회", description = "<주문도서> 페이지로 분류된 주문도서 조회합니다.")
	ResponseEntity<Page<GetOrdersBookResponse>> getOrdersBookAll(@CurrentMembers Long memberId, Pageable pageable);

	/**
	 * 특정 <주문도서>를 삭제하는 API입니다.
	 *
	 * @param id 주문도서 ID
	 * @return 없음
	 */
	@Operation(summary = "<주문도서> 삭제", description = "<주문도서> 삭제 합니다.")
	ResponseEntity<Void> deleteOrdersBook(@PathVariable Long id);

	/**
	 * 회원 ID로 <주문도서>를 전체 조회하는 API입니다.
	 *
	 * @param memberId 회원 ID
	 * @param pageable 페이지 요청 객체
	 * @return 페이지로 분류된 주문도서 리뷰 ID 응답 객체
	 */
	@Operation(summary = "<주문도서> 회원 조회", description = "<주문도서> 회원으로 전체 조회합니다.")
	@GetMapping("/members")
	ResponseEntity<Page<GetOrdersBookReviewIdResponse>> getOrdersBookReviewIdByMemberId(@CurrentMembers Long memberId,
		Pageable pageable);
}
