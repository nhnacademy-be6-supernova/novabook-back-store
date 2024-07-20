package store.novabook.store.member.controller.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.orders.dto.request.GetGuestOrderHistoryRequest;
import store.novabook.store.orders.dto.response.GetOrderDetailResponse;

/**
 * 비회원 관련 API 요청을 처리하는 컨트롤러의 문서화 인터페이스입니다.
 * 비회원의 주문 내역을 조회하는 기능을 제공합니다.
 */
@Tag(name = "Guest API", description = "비회원 API")
public interface GuestControllerDocs {
	/**
	 * 비회원 주문 내역을 조회합니다.
	 *
	 * @param request 비회원 주문 내역 조회 요청 데이터
	 * @return ResponseEntity<GetOrderDetailResponse> 주문 내역 정보와 HTTP 상태 코드를 포함한 응답
	 */
	@Operation(summary = "비회원 주문 조회", description = "비회원의 주문 내역을 조회합니다.")
	@Parameter(description = "비회원 주문 내역 조회 요청 데이터", required = true)
	ResponseEntity<GetOrderDetailResponse> getOrder(@Valid @RequestBody GetGuestOrderHistoryRequest request);

}
