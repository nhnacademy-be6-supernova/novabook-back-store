package store.novabook.store.orders.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import store.novabook.store.book.dto.response.GetOrdersBookReviewIdResponse;
import store.novabook.store.common.security.aop.CurrentMembers;
import store.novabook.store.orders.dto.response.GetOrderDetailResponse;
import store.novabook.store.orders.dto.response.GetOrdersBookResponse;

/**
 * 주문 도서 관련 API 문서화 인터페이스입니다.
 * 이 인터페이스는 주문 도서 관련 API를 문서화하는 데 사용됩니다.
 */
@Tag(name = "OrdersBook API", description = "주문-도서 관계테이블 API")
public interface OrdersBookControllerDocs {

	/**
	 * 회원 ID로 주문된 도서 목록을 페이지 단위로 조회합니다.
	 *
	 * @param memberId 회원 ID
	 * @param pageable 페이지 정보
	 * @return 주문된 도서 목록의 페이지 응답
	 */
	@Operation(summary = "회원의 주문 도서 조회", description = "회원 ID로 주문된 도서 목록을 페이지 단위로 조회합니다.")
	@Parameter(name = "memberId", description = "회원 ID", required = true)
	@Parameter(name = "pageable", description = "페이지 정보", required = true)
	ResponseEntity<Page<GetOrdersBookResponse>> getOrdersBookAll(@CurrentMembers Long memberId, Pageable pageable);

	/**
	 * 주문 ID로 주문 세부 정보를 조회합니다.
	 *
	 * @param ordersId 주문 ID
	 * @return 주문 세부 정보에 대한 응답
	 */
	@Operation(summary = "주문 세부 정보 조회", description = "주문 ID로 주문 세부 정보를 조회합니다.")
	@Parameter(name = "ordersId", description = "주문 ID", required = true)
	ResponseEntity<GetOrderDetailResponse> getOrderDetails(@PathVariable Long ordersId);

	/**
	 * 회원 ID로 주문한 도서의 리뷰 ID를 페이지 단위로 조회합니다.
	 *
	 * @param memberId 회원 ID
	 * @param pageable 페이지 정보
	 * @return 주문 도서 리뷰 ID 목록의 페이지 응답
	 */
	@Operation(summary = "회원의 주문 도서 리뷰 ID 조회", description = "마이페이지에서 회원 ID로 주문한 도서의 리뷰 ID를 페이지 단위로 조회합니다.")
	@Parameter(name = "memberId", description = "회원 ID", required = true)
	@Parameter(name = "pageable", description = "페이지 정보", required = true)
	ResponseEntity<Page<GetOrdersBookReviewIdResponse>> getOrdersBookReviewIdByMemberId(@CurrentMembers Long memberId,
		Pageable pageable);
}
