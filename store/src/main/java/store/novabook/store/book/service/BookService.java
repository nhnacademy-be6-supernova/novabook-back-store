package store.novabook.store.book.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.handler.exception.EntityNotFoundException;
import store.novabook.handler.exception.ErrorStatus;
import store.novabook.store.book.dto.CreateBookRequest;
import store.novabook.store.book.dto.CreateBookResponse;
import store.novabook.store.book.dto.GetBookRequest;
import store.novabook.store.book.dto.GetBookResponse;
import store.novabook.store.book.entity.Author;
import store.novabook.store.book.entity.AuthorBook;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.BookDiscountRate;
import store.novabook.store.book.entity.BookStatus;
import store.novabook.store.book.entity.BookTag;
import store.novabook.store.book.entity.Tag;
import store.novabook.store.book.repository.AuthorBookRepository;
import store.novabook.store.book.repository.AuthorRepository;
import store.novabook.store.book.repository.BookDiscountRateRepository;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.book.repository.BookTagRepository;
import store.novabook.store.book.repository.LikesRepository;
import store.novabook.store.book.repository.TagRepository;
import store.novabook.store.category.entity.BookCategory;
import store.novabook.store.category.entity.Category;
import store.novabook.store.category.repository.BookCategoryRepository;
import store.novabook.store.category.repository.CategoryRepository;
import store.novabook.store.point.entity.PointPolicy;
import store.novabook.store.point.repository.PointPolicyRepository;

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
