package store.novabook.store.book.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto.AuthorResponse;
import store.novabook.store.book.dto.CreateBookRequest;
import store.novabook.store.book.dto.GetBookResponse;
import store.novabook.store.book.service.AuthorService;
import store.novabook.store.book.service.BookService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
	private final BookService bookService;
	private final AuthorService authorService;


	@GetMapping("/book/{id}")
	public ResponseEntity<GetBookResponse> getBook(@PathVariable Long id) {
		return ResponseEntity.ok().body(bookService.getBook(id));
	}

	@PostMapping("/create")
	public ResponseEntity<Void> createBook(CreateBookRequest createBookRequest) {
		bookService.create(createBookRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}

	//책의 저자들
	@GetMapping("/book/{id}/authors")
	public ResponseEntity<List<AuthorResponse>> getBookAuthors(@PathVariable Long id) {
		List<AuthorResponse> authorResponses = authorService.getAuthorByBookId(id);
		return ResponseEntity.ok().body(authorResponses);
	}
}
