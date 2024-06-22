package store.novabook.store.orders.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.orders.dto.CreateDeliveryFeeRequest;
import store.novabook.store.orders.dto.CreateResponse;
import store.novabook.store.orders.dto.GetDeliveryFeeResponse;
import store.novabook.store.orders.service.DeliveryFeeService;

@Tag(name = "DeliveryFee API", description = "DeliveryFee 을 생성 조회 수정, 삭제 합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/orders/delivery/fee")
public class DeliveryFeeController {
	private final DeliveryFeeService deliveryFeeService;

	//생성
	@Operation(summary = "생성", description = "생성 합니다 ")
	@PostMapping
	public ResponseEntity<CreateResponse> createDeliveryFee(@Valid @RequestBody CreateDeliveryFeeRequest request) {
		CreateResponse response = deliveryFeeService.createFee(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	//전체 조회
	@Operation(summary = "전체 조회", description = "전체 조회 합니다.")
	@GetMapping
	public ResponseEntity<Page<GetDeliveryFeeResponse>> getDeliveryFeeAll() {
		Page<GetDeliveryFeeResponse> deliveryFeeResponses = deliveryFeeService.findAllDeliveryFees();
		return ResponseEntity.ok().body(deliveryFeeResponses);
	}

	//단건 조회
	@Operation(summary = "조회", description = "조회 합니다.")
	@GetMapping("/{id}")
	public ResponseEntity<GetDeliveryFeeResponse> getDeliveryFee(@PathVariable Long id) {
		GetDeliveryFeeResponse response = deliveryFeeService.getDeliveryFee(id);
		return ResponseEntity.ok().body(response);
	}
}
