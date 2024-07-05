package store.novabook.store.orders.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.orders.controller.docs.DeliveryFeeControllerDocs;
import store.novabook.store.orders.dto.request.CreateDeliveryFeeRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetDeliveryFeeListResponse;
import store.novabook.store.orders.dto.response.GetDeliveryFeeResponse;
import store.novabook.store.orders.service.DeliveryFeeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/orders/delivery-fee")
public class DeliveryFeeController implements DeliveryFeeControllerDocs {

	private final DeliveryFeeService deliveryFeeService;

	@PostMapping
	public ResponseEntity<CreateResponse> createDeliveryFee(@Valid @RequestBody CreateDeliveryFeeRequest request) {
		CreateResponse response = deliveryFeeService.createFee(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping(params = {"size", "page"})
	public ResponseEntity<Page<GetDeliveryFeeResponse>> getDeliveryFeeAll(Pageable pageable) {
		Page<GetDeliveryFeeResponse> deliveryFeeResponses = deliveryFeeService.findAllDeliveryFees(pageable);
		return ResponseEntity.ok().body(deliveryFeeResponses);
	}

	@GetMapping
	public ResponseEntity<GetDeliveryFeeListResponse> getDeliveryFeeAllList() {
		List<GetDeliveryFeeResponse> deliveryFeeResponses = deliveryFeeService.findAllDeliveryFeeList();
		GetDeliveryFeeListResponse getDeliveryFeeListResponse = GetDeliveryFeeListResponse.builder()
			.getDeliveryFeeResponses(deliveryFeeResponses).build();
		return ResponseEntity.ok().body(getDeliveryFeeListResponse);
	}

	@GetMapping("/recent")
	public ResponseEntity<GetDeliveryFeeResponse> getRecentDeliveryFee() {
		return ResponseEntity.ok(deliveryFeeService.getRecentDeliveryFee());
	}


	@GetMapping("/{id}")
	public ResponseEntity<GetDeliveryFeeResponse> getDeliveryFee(@PathVariable Long id) {
		return ResponseEntity.ok().body(deliveryFeeService.getDeliveryFee(id));
	}
}
