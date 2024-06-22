package store.novabook.store.book.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
import store.novabook.store.book.dto.CreateBookRequest;
import store.novabook.store.book.dto.GetBookAllResponse;
import store.novabook.store.book.dto.GetBookResponse;
import store.novabook.store.book.dto.UpdateBookRequest;
import store.novabook.store.book.service.BookService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/books")
public class BookController {
	private final BookService bookService;

	@GetMapping("/{id}")
	public ResponseEntity<GetBookResponse> getBook(@PathVariable Long id) {
		return ResponseEntity.ok().body(bookService.getBook(id));

	}

	@GetMapping
	public ResponseEntity<Page<GetBookAllResponse>> getBookAll(Pageable pageable) {
		return ResponseEntity.ok().body(bookService.getBookAll(pageable));
	}

	@PostMapping
	public ResponseEntity<Void> createBook(@Valid @RequestBody CreateBookRequest createBookRequest) {
		bookService.create(createBookRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping
	public ResponseEntity<Void> updateBook(@Valid @RequestBody UpdateBookRequest updateBookRequest) {
		bookService.update(updateBookRequest);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
		bookService.delete(id);
		return ResponseEntity.ok().build();
	}

}
