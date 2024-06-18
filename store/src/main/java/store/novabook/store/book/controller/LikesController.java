package store.novabook.store.book.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto.MiniBookResponse;
import store.novabook.store.book.service.LikesService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class LikesController {
	private final LikesService likesService;

	//member id 가 좋아요 누른 책들
	@GetMapping("/{id}/likes")
	public ResponseEntity<List<MiniBookResponse>> like(@PathVariable Long id) {
		List<MiniBookResponse> miniBookResponses = likesService.myLikes(id);
		return ResponseEntity.ok().body(miniBookResponses);
	}
}
