package store.novabook.store.orders.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import store.novabook.store.common.security.aop.CheckRole;
import store.novabook.store.orders.controller.docs.WrappingPaperControllerDocs;
import store.novabook.store.orders.dto.request.CreateWrappingPaperRequest;
import store.novabook.store.orders.dto.request.UpdateWrappingPaperRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetWrappingPaperAllResponse;
import store.novabook.store.orders.dto.response.GetWrappingPaperResponse;
import store.novabook.store.orders.service.WrappingPaperService;

@RestController
@RequestMapping("/api/v1/store/orders/wrapping-papers")
@RequiredArgsConstructor
public class WrappingPaperController implements WrappingPaperControllerDocs {
	private final WrappingPaperService wrappingPaperService;

	@CheckRole("ROLE_ADMIN")
	@PostMapping
	public ResponseEntity<CreateResponse> createWrappingPaper(@RequestBody CreateWrappingPaperRequest request) {
		return ResponseEntity.ok(wrappingPaperService.createWrappingPaper(request));
	}

	@CheckRole("ROLE_ADMIN")
	@GetMapping
	public ResponseEntity<GetWrappingPaperAllResponse> getWrappingPaperAllList() {
		GetWrappingPaperAllResponse response = GetWrappingPaperAllResponse.builder()
			.getWrappingPaperResponse(wrappingPaperService.getWrappingPaperAllList())
			.build();
		return ResponseEntity.ok(response);
	}

	@CheckRole("ROLE_ADMIN")
	@GetMapping("/{id}")
	public ResponseEntity<GetWrappingPaperResponse> getWrappingPaper(@PathVariable Long id) {
		GetWrappingPaperResponse response = wrappingPaperService.getWrappingPaperById(id);
		return ResponseEntity.ok(response);
	}

	@CheckRole("ROLE_ADMIN")
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateWrappingPaper(@PathVariable Long id,
		@Valid @RequestBody UpdateWrappingPaperRequest request) {
		wrappingPaperService.updateWrappingPaper(id, request);
		return ResponseEntity.ok().build();
	}
}
