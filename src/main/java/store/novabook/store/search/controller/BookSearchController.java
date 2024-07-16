package store.novabook.store.search.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.search.document.BookDocument;
import store.novabook.store.search.dto.GetBookSearchResponse;
import store.novabook.store.search.service.impl.BookSearchServiceImpl;

@RestController
@RequestMapping("/api/v1/store/search")
@RequiredArgsConstructor
public class BookSearchController {

	private final BookSearchServiceImpl bookSearchService;

	@GetMapping("/keyword")
	public Page<GetBookSearchResponse> searchByKeyword(@RequestParam String title, Pageable pageable) {
		return bookSearchService.searchByKeywordContaining(title, pageable);
	}

	@GetMapping("/author")
	public Page<GetBookSearchResponse> searchByAuthor(@RequestParam String author, Pageable pageable) {
		return bookSearchService.searchByAuthorContaining(author, pageable);
	}

	@GetMapping("/publish")
	public Page<GetBookSearchResponse> searchByPublish(@RequestParam String publish, Pageable pageable) {
		return bookSearchService.searchByPublishContaining(publish, pageable);
	}

	@GetMapping("/category")
	public Page<GetBookSearchResponse> searchByCategory(@RequestParam String category, Pageable pageable) {
		return bookSearchService.searchByCategoryListContaining(category, pageable);
	}

	@GetMapping()
	public List<BookDocument> searchByTagsContaining(Pageable pageable) {
		return bookSearchService.searchByTagsContaining(pageable);
	}
}
