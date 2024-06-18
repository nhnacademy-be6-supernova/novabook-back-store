package store.novabook.store.book.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.entity.BookStatus;
import store.novabook.store.book.repository.BookStatusRepository;
import store.novabook.store.exception.EntityNotFoundException;
import store.novabook.store.book.dto.CreateBookRequest;
import store.novabook.store.book.dto.GetBookResponse;
import store.novabook.store.book.dto.UpdateBookRequest;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.BookTag;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.book.repository.BookTagRepository;
import store.novabook.store.book.repository.LikesRepository;
import store.novabook.store.category.entity.BookCategory;
import store.novabook.store.category.repository.BookCategoryRepository;

@Service
@RequiredArgsConstructor
public class BookService {
	private final BookRepository bookRepository;
	private final BookStatusRepository bookStatusRepository;
	private final LikesRepository likesRepository;
	private final BookTagRepository bookTagRepository;
	private final BookCategoryRepository bookCategoryRepository;


	public void create(CreateBookRequest request) {
		BookStatus bookStatus = bookStatusRepository.findById(request.bookStatusId())
			.orElseThrow(() -> new EntityNotFoundException(BookStatus.class, request.bookStatusId()));
		Book book = Book.of(request, bookStatus);
		bookRepository.save(book);
	}

	public GetBookResponse getBook(Long id) {
		Book book = bookRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Book.class, id));

		List<BookTag> bookTags = bookTagRepository.findAllByBookId(book.getId());
		List<BookCategory> bookCategories = bookCategoryRepository.findAllByBookId(book.getId());

		int likes = likesRepository.countByBookId(book.getId());

		return GetBookResponse.fromEntity(book, bookTags, bookCategories, likes);
	}

	public void update(UpdateBookRequest request) {

	}

	public void delete(Long id) {
	}
}
