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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.orders.dto.CreateResponse;
import store.novabook.store.orders.dto.CreateWrappingPaperRequest;
import store.novabook.store.orders.dto.GetWrappingPaperAllResponse;
import store.novabook.store.orders.dto.GetWrappingPaperResponse;
import store.novabook.store.orders.dto.UpdateWrappingPaperRequest;
import store.novabook.store.orders.service.WrappingPaperService;

@Tag(name = "WrappingPaper API", description = "WrappingPaper 을 생성, 조회, 수정합니다. ")
@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class WrappingPaperController {
	private final WrappingPaperService wrappingPaperService;

	@Operation(summary = "WrappingPaper 생성", description = "WrappingPaper 생성합니다.")
	@PostMapping
	public ResponseEntity<CreateResponse> createWrappingPaper(@RequestBody CreateWrappingPaperRequest request) {
		return ResponseEntity.ok(wrappingPaperService.createWrappingPaper(request));
	}

	@Operation(summary = "WrappingPaper 전체 조회", description = "WrappingPaper 전체 조회합니다. 페이저블로 받습니다")
	@GetMapping("/pageable")
	public ResponseEntity<Page<GetWrappingPaperResponse>> getWrappingPaperAll(Pageable pageable) {
		Page<GetWrappingPaperResponse> response = wrappingPaperService.getWrappingPaperAll(pageable);
		return ResponseEntity.ok(response);
	}

	//List 전체조회
	@Operation(summary = "WrappingPaper 전체 조회", description = "WrappingPaper 전체 조회합니다. 리스트로 받습니다.")
	@GetMapping
	public ResponseEntity<GetWrappingPaperAllResponse> getWrappingPaperAllList() {
		GetWrappingPaperAllResponse response = GetWrappingPaperAllResponse.builder()
			.getWrappingPaperResponse(wrappingPaperService.getWrappingPaperAllList())
			.build();
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "조회", description = "조회합니다.")
	@GetMapping("/{id}")
	public ResponseEntity<GetWrappingPaperResponse> getWrappingPaper(@PathVariable Long id) {
		GetWrappingPaperResponse response = wrappingPaperService.getWrappingPaperById(id);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "수정", description = "수정합니다.")
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateWrappingPaper(@PathVariable Long id,
		@Valid @RequestBody UpdateWrappingPaperRequest request) {
		wrappingPaperService.updateWrappingPaper(id, request);
		return ResponseEntity.ok().build();
	}
}
