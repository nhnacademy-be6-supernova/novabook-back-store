package store.novabook.store.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.member.service.GuestService;
import store.novabook.store.orders.dto.request.GetGuestOrderHistoryRequest;
import store.novabook.store.orders.dto.response.GetOrderDetailResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/store/guest")
public class GuestController {

	private final GuestService guestService;

	@PostMapping
	public ResponseEntity<GetOrderDetailResponse> getOrder(@RequestBody GetGuestOrderHistoryRequest request) {
		return ResponseEntity.ok().body(guestService.getOrderGuest(request));
	}
}
