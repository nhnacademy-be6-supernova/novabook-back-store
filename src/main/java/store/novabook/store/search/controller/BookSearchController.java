package store.novabook.store.search.controller;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.search.document.BookDocument;
import store.novabook.store.search.service.BookSearchService;

@RestController
@RequestMapping("/api/v1/store/search")
@Document(indexName = "books")
@RequiredArgsConstructor
public class BookSearchController {

	private final BookSearchService bookSearchService;

	@GetMapping("/title")
	public List<BookDocument> searchByTitle(@RequestParam String title) {
		return bookSearchService.searchByTitlePhrase(title);
	}

	@GetMapping("/author")
	public List<BookDocument> searchByAuthor(@RequestParam String author) {
		return bookSearchService.searchByAuthorContaining(author);
	}
}
