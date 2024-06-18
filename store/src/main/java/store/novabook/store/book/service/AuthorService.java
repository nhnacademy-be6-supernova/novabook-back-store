package store.novabook.store.book.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto2.AuthorBookDto;
import store.novabook.store.book.entity.AuthorBook;
import store.novabook.store.book.repository.AuthorBookRepository;
import store.novabook.store.book.repository.AuthorRepository;
import store.novabook.store.book.repository.BookRepository;

@Service
@RequiredArgsConstructor
public class AuthorService {
	private final AuthorBookRepository repository;
	private final AuthorRepository authorRepository;
	private final BookRepository bookRepository;

	// 저자가 작성한 한 책들 AuthorBookDto list로 리턴
	public List<AuthorBookDto> getBooksByAuthorId(Long authorId) {
		List<AuthorBook> authorBooks = repository.findByAuthorId(authorId);
		return authorBooks.stream()
			.map(authorBook -> new AuthorBookDto(authorBook.getAuthor().getId(), authorBook.getBook().getId()))
			.collect(Collectors.toList());
	}

	//책으로 book 안에 저자들 AuthorBookDto list로 리턴
	public List<AuthorBookDto> getAuthorByBookId(Long bookId) {
		List<AuthorBook> authorBooks = repository.findByBookId(bookId);
		return authorBooks.stream()
			.map(authorBook -> new AuthorBookDto(authorBook.getAuthor().getId(), authorBook.getBook().getId()))
			.collect(Collectors.toList());
	}

	public AuthorBook save(AuthorBook authorBook) {
		return repository.save(authorBook);
	}

	public AuthorBook findById(Long id) {
		return repository.findById(id).orElse(null);
	}

}
