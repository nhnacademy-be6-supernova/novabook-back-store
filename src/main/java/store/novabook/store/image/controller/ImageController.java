package store.novabook.store.image.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.image.dto.request.CreateBookImageRequest;
import store.novabook.store.image.dto.request.CreateImageRequest;
import store.novabook.store.image.dto.request.CreateReviewImageRequest;
import store.novabook.store.image.service.BookImageService;
import store.novabook.store.image.service.ImageService;
import store.novabook.store.image.service.ReviewImageService;
import store.novabook.store.orders.dto.CreateResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/images")
public class ImageController {
	private final ImageService imageService;
	private final BookImageService bookImageService;
	private final ReviewImageService reviewImageService;
	// 이미지 생성
	@PostMapping
	public ResponseEntity<CreateResponse> createImage(@Valid @RequestBody CreateImageRequest request) {
		return ResponseEntity.ok(imageService.createImage(request));
	}

	//책 이미지 생성
	@PostMapping("/book")
	public ResponseEntity<CreateResponse> createImage(@Valid @RequestBody CreateBookImageRequest request) {
		return ResponseEntity.ok(bookImageService.createBookImage(request));
	}

	//리뷰 이미지 생성
	@PostMapping("/review")
	public ResponseEntity<CreateResponse> createImage(@Valid @RequestBody CreateReviewImageRequest request) {
		return ResponseEntity.ok(reviewImageService.createReviewImage(request));
	}

}
