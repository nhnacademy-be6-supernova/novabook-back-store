package store.novabook.store.orders.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.orders.dto.CreateResponse;
import store.novabook.store.orders.dto.CreateWrappingPaperRequest;
import store.novabook.store.orders.dto.GetWrappingPaperResponse;
import store.novabook.store.orders.dto.UpdateWrappingPaperRequest;
import store.novabook.store.orders.service.WrappingPaperService;

@RestController
@RequestMapping("/wrapping/paper")
@RequiredArgsConstructor
public class WrappingPaperController {
	private final WrappingPaperService wrappingPaperService;

	@PostMapping
	public ResponseEntity<CreateResponse> createWrappingPaper(@RequestBody CreateWrappingPaperRequest request) {
		return ResponseEntity.ok(wrappingPaperService.createWrappingPaper(request));
	}

	@GetMapping
	public ResponseEntity<Page<GetWrappingPaperResponse>> getWrappingPaperAll() {
		Page<GetWrappingPaperResponse> response = wrappingPaperService.getWrappingPaperAll();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<GetWrappingPaperResponse> getWrappingPaper(@PathVariable Long id) {
		GetWrappingPaperResponse response = wrappingPaperService.getWrappingPaperById(id);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateWrappingPaper(@PathVariable Long id,
		@Valid @RequestBody UpdateWrappingPaperRequest request) {
		wrappingPaperService.updateWrappingPaper(id, request);
		return ResponseEntity.noContent().build();
	}
}
