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
import store.novabook.store.orders.dto.request.UpdateOrdersBookRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetOrdersBookResponse;

@Tag(name = "OrdersBook API", description = "주문-도서 관계테이블 API")
public interface OrdersBookControllerDocs {

	//생성
	@Operation(summary = "<주문도서> 생성", description = "<주문도서>를 생성합니다 ")
	ResponseEntity<CreateResponse> createOrdersBook(@Valid @RequestBody CreateOrdersBookRequest request);

	//전체 조회
	@Operation(summary = "<주문도서> 전체 조회", description = "<주문도서> 전체 조회합니다.")
	ResponseEntity<Page<GetOrdersBookResponse>> getOrdersBookAll();

	//단건 조회
	@Operation(summary = "<주문도서> 조회", description = "<주문도서>를 조회합니다.")
	ResponseEntity<GetOrdersBookResponse> getOrdersBook(@PathVariable Long id);

	//수정
	@Operation(summary = "<주문도서> 수정", description = "<주문도서> 수정합니다.")
	ResponseEntity<Void> updateOrdersBook(@PathVariable Long id,
		@Valid @RequestBody UpdateOrdersBookRequest request);

	//삭제
	@Operation(summary = "<주문도서> 삭제", description = "<주문도서> 삭제 합니다.")
	ResponseEntity<Void> deleteOrdersBook(@PathVariable Long id);

	@Operation(summary = "<주문도서> 회원 조회", description = "<주문도서> 회원으로 전체 조회합니다.")
	@GetMapping("/members")
	ResponseEntity<Page<GetOrdersBookReviewIdResponse>> getOrdersBookReviewIdByMemberId(@CurrentMembers Long memberId,
		Pageable pageable);
}
