package store.novabook.store.book.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.handler.exception.EntityNotFoundException;
import store.novabook.store.book.dto.CreateBookRequest;
import store.novabook.store.book.dto.CreateBookResponse;
import store.novabook.store.book.dto.GetBookResponse;
import store.novabook.store.book.entity.AuthorBook;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.BookDiscountRate;
import store.novabook.store.book.entity.BookTag;
import store.novabook.store.book.repository.AuthorBookRepository;
import store.novabook.store.book.repository.BookDiscountRateRepository;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.book.repository.BookTagRepository;
import store.novabook.store.book.repository.LikesRepository;
import store.novabook.store.category.entity.BookCategory;
import store.novabook.store.category.repository.BookCategoryRepository;

@Service
@RequiredArgsConstructor
public class BookService {
	private final BookRepository bookRepository;
	private final AuthorBookRepository authorBookRepository;
	private final LikesRepository likesRepository;
	private final BookDiscountRateRepository bookDiscountRateRepository;
	private final BookTagRepository bookTagRepository;
	private final BookCategoryRepository bookCategoryRepository;


	public CreateBookResponse create(CreateBookRequest request) {
		Book book = Book.of(request);
		Book newBook = bookRepository.save(book);
		return CreateBookResponse.fromEntity(newBook);
	}

	public GetBookResponse getBook(Long id) {
		Book book = bookRepository.findById(id).orElse(null);
		if(book == null) {
			throw new EntityNotFoundException(Book.class, id);
		}
		List<AuthorBook> authors = authorBookRepository.findByBookId(book.getId());
		List<BookTag> bookTags = bookTagRepository.findAllByBookId(book.getId());
		List<BookCategory> bookCategories = bookCategoryRepository.findAllByBookId(book.getId());
		BookDiscountRate bookDiscountRate = bookDiscountRateRepository.findByBookId(book.getId());
		int likes = likesRepository.countByBookId(book.getId());

		return GetBookResponse.fromEntity(book, authors, bookDiscountRate, bookTags, bookCategories,likes);
	}
}
