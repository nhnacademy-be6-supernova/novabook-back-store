package store.novabook.store.orders.controller.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import store.novabook.store.orders.dto.request.PaymentRequest;

/**
 * 주문 트랜잭션 관련 API를 문서화하는 인터페이스입니다.
 */
public interface OrdersSagaControllerDocs {
	/**
	 * @param paymentRequest 결제 시스템에서 넘어온 데이터
	 * @return HTTP 상태 코드
	 */
	@Operation(summary = "주문 트랜잭션 시작", description = "결제 시스템에서 넘어온 데이터를 사용하여 주문 트랜잭션을 시작합니다.")
	@Parameter(description = "결제 시스템에서 넘어온 데이터", required = true)
	ResponseEntity<Void> createOrder(@RequestBody PaymentRequest paymentRequest);
}
