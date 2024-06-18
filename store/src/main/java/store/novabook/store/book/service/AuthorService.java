package store.novabook.store.book.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto2.AuthorBookDto;
import store.novabook.store.book.dto2.AuthorResponse;
import store.novabook.store.book.dto2.BookResponse;
import store.novabook.store.book.entity.Author;
import store.novabook.store.book.entity.AuthorBook;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.repository.AuthorBookRepository;
import store.novabook.store.book.repository.AuthorRepository;
import store.novabook.store.book.repository.BookRepository;

@Service
@RequiredArgsConstructor
public class AuthorService {
	private final AuthorBookRepository repository;
	private final AuthorRepository authorRepository;
	private final BookRepository bookRepository;


	//책으로 book 안에 저자들 AuthorBookDto list로 리턴
	public List<AuthorBookDto> getAuthorByBookId(Long bookId) {
		List<AuthorBook> authorBooks = repository.findByBookId(bookId);
		return authorBooks.stream()
			.map(authorBook -> new AuthorBookDto(authorBook.getAuthor().getId(), authorBook.getBook().getId()))
			.collect(Collectors.toList());
	}

	//bookAuthor에 작가들을 받아옴
	public ResponseEntity<List<AuthorResponse>> toAuthorResponses(List<AuthorBookDto> authorBookDtos) {
		List<AuthorResponse> authorResponses = authorBookDtos.stream()
			.map(authorBookDto -> {
				Optional<Author> optionalAuthor = authorRepository.findById(authorBookDto.authorId());
				Author author = optionalAuthor.get();
				return new AuthorResponse(
					author.getId(),
					author.getName(),
					author.getDescription(),
					author.getRole(),
					author.getCreatedAt(),
					author.getUpdatedAt()
				);
			})
			.collect(Collectors.toList());
		return ResponseEntity.ok(authorResponses);
	}

	// 저자가 작성한 한 책들 AuthorBookDto list로 리턴
	public List<AuthorBookDto> getBooksByAuthorId(Long authorId) {
		List<AuthorBook> authorBooks = repository.findByAuthorId(authorId);
		return authorBooks.stream()
			.map(authorBook -> new AuthorBookDto(authorBook.getAuthor().getId(), authorBook.getBook().getId()))
			.collect(Collectors.toList());
	}

	//bookAuthor에서 책들을 받아옴
	public ResponseEntity<List<BookResponse>> toBookResponses(List<AuthorBookDto> authorBookDtos) {
		List<BookResponse> bookResponses = authorBookDtos.stream()
			.map(authorBookDto -> {
				Optional<Book> optionalBook = bookRepository.findById(authorBookDto.bookId());
				Book book = optionalBook.get();
				return new BookResponse(
					book.getId(),
					book.getBookStatus().getId(),
					book.getIsbn(),
					book.getTitle(),
					book.getSubTitle(),
					book.getEngTitle(),
					book.getIndex(),
					book.getExplanation(),
					book.getTranslator(),
					book.getPublisher(),
					book.getPublicationDate(),
					book.getInventory(),
					book.getPrice(),
					book.isPackaged(),
					book.getImage(),
					book.getCreatedAt(),
					book.getUpdatedAt()
				);
			})
			.collect(Collectors.toList());
		return ResponseEntity.ok(bookResponses);

	}

	//작가 등록
	public AuthorBook save(AuthorBook authorBook) {
		return repository.save(authorBook);
	}


	//작가 업데이트
	public void updateAuthor(AuthorBook authorBook) {
		repository.save(authorBook);
	}



}
