package store.novabook.store.orders.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto.response.GetOrdersBookReviewIdResponse;
import store.novabook.store.common.security.aop.CheckRole;
import store.novabook.store.common.security.aop.CurrentMembers;
import store.novabook.store.orders.dto.response.GetOrderDetailResponse;
import store.novabook.store.orders.dto.response.GetOrdersBookResponse;
import store.novabook.store.orders.service.OrdersBookService;

@RestController
@RequestMapping("/api/v1/store/orders/book")
@RequiredArgsConstructor
public class OrdersBookController {

	private final OrdersBookService ordersBookService;

	@CheckRole({"ROLE_ADMIN", "ROLE_MEMBERS"})
	@GetMapping("/member/orders")
	public ResponseEntity<Page<GetOrdersBookResponse>> getOrdersBookAll(@CurrentMembers Long memberId,
		Pageable pageable) {
		Page<GetOrdersBookResponse> responses = ordersBookService.getOrdersBookByMemberId(memberId, pageable);
		return ResponseEntity.ok(responses);
	}

	@CheckRole({"ROLE_ADMIN", "ROLE_MEMBERS"})
	@GetMapping("/detail/{ordersId}")
	public ResponseEntity<GetOrderDetailResponse> getOrderDetails(@PathVariable Long ordersId) {
		GetOrderDetailResponse response = ordersBookService.getOrderDetail(ordersId);
		return ResponseEntity.ok().body(response);
	}

	//마이페이지에서 사용
	@CheckRole({"ROLE_ADMIN", "ROLE_MEMBERS"})
	@GetMapping("/members")
	public ResponseEntity<Page<GetOrdersBookReviewIdResponse>> getOrdersBookReviewIdByMemberId(
		@CurrentMembers Long memberId, Pageable pageable) {
		Page<GetOrdersBookReviewIdResponse> responses = ordersBookService.getOrdersBookReviewByMemberId(memberId,
			pageable);
		return ResponseEntity.ok(responses);
	}

}
