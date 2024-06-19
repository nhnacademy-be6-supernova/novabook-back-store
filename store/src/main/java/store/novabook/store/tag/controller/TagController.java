package store.novabook.store.tag.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.tag.dto.CreateTagRequest;
import store.novabook.store.tag.dto.CreateTagResponse;
import store.novabook.store.tag.dto.GetTagResponse;
import store.novabook.store.tag.dto.UpdateTagRequest;
import store.novabook.store.tag.service.TagService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {

	private final TagService tagService;

	@GetMapping
	public ResponseEntity<Page<GetTagResponse>> getTagAll(Pageable pageable) {
		return ResponseEntity.ok().body(tagService.getTagAll(pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<GetTagResponse> getTag(@PathVariable Long id) {
		return ResponseEntity.ok().body(tagService.getTag(id));
	}

	@PostMapping
	public ResponseEntity<CreateTagResponse> createTag(@RequestBody @Valid CreateTagRequest createTagRequest) {
		return ResponseEntity.ok().body(tagService.createTag(createTagRequest));
	}

	@PutMapping
	public ResponseEntity<Void> updateTag(@RequestBody UpdateTagRequest updateTagRequest) {
		tagService.updateTag(updateTagRequest);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
		tagService.deleteTag(id);
		return ResponseEntity.ok().build();
	}

}
