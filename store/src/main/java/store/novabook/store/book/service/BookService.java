package store.novabook.store.book.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto.CreateBookRequest;
import store.novabook.store.book.dto.GetBookAllResponse;
import store.novabook.store.book.dto.GetBookResponse;
import store.novabook.store.book.dto.UpdateBookRequest;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.BookStatus;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.book.repository.BookStatusRepository;
import store.novabook.store.book.repository.LikesRepository;
import store.novabook.store.category.entity.BookCategory;
import store.novabook.store.category.entity.Category;
import store.novabook.store.category.repository.BookCategoryRepository;
import store.novabook.store.category.repository.CategoryRepository;
import store.novabook.store.exception.EntityNotFoundException;
import store.novabook.store.tag.entity.BookTag;
import store.novabook.store.tag.entity.Tag;
import store.novabook.store.tag.repository.BookTagRepository;
import store.novabook.store.tag.repository.TagRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {
	private final BookRepository bookRepository;
	private final BookStatusRepository bookStatusRepository;
	private final LikesRepository likesRepository;
	private final BookTagRepository bookTagRepository;
	private final CategoryRepository categoryRepository;
	private final TagRepository tagRepository;
	private final BookCategoryRepository bookCategoryRepository;

	public void create(CreateBookRequest request) {
		BookStatus bookStatus = bookStatusRepository.findById(request.bookStatusId())
			.orElseThrow(() -> new EntityNotFoundException(BookStatus.class, request.bookStatusId()));

		Book book = bookRepository.save(Book.of(request, bookStatus));

		List<Tag> tags = tagRepository.findByIdIn(request.tags());
		List<BookTag> bookTags = tags.stream()
			.map(tag -> new BookTag(book, tag))
			.collect(Collectors.toList());
		bookTagRepository.saveAll(bookTags);

		List<BookCategory> bookCategories = new ArrayList<>();
		for (Map.Entry<Integer, Long> entry : request.category().entrySet()) {
			Integer depth = entry.getKey();
			Long categoryId = entry.getValue();

			Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new EntityNotFoundException(Category.class, categoryId));
			BookCategory bookCategory = new BookCategory(book, category, depth);
			bookCategories.add(bookCategory);
		}
		bookCategoryRepository.saveAll(bookCategories);
	}

	public GetBookResponse getBook(Long id) {
		Book book = bookRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Book.class, id));

		List<BookTag> bookTags = bookTagRepository.findAllByBookId(book.getId());
		List<BookCategory> bookCategories = bookCategoryRepository.findAllByBookId(book.getId());

		int likes = likesRepository.countByBookId(book.getId());

		return GetBookResponse.fromEntity(book, bookTags, bookCategories, likes);
	}

	public Page<GetBookAllResponse> getBookAll(Pageable pageable) {
		Page<Book> books = bookRepository.findAll(pageable);
		Page<GetBookAllResponse> booksResponse = books.map(GetBookAllResponse::fromEntity);

		return new PageImpl<>(booksResponse.getContent(), pageable, books.getTotalElements());
	}

	public void update(UpdateBookRequest request) {
		BookStatus bookStatus = bookStatusRepository.findById(request.bookStatusId())
			.orElseThrow(() -> new EntityNotFoundException(BookStatus.class, request.bookStatusId()));

		Book book = bookRepository.findById(request.id())
			.orElseThrow(() -> new EntityNotFoundException(Book.class, request.id()));

		book.update(bookStatus, request);
	}

	public void delete(Long id) {
		BookStatus bookStatus = bookStatusRepository.findById(4L)
			.orElseThrow(() -> new EntityNotFoundException(BookStatus.class, id));

		Book book = bookRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Book.class, id));
		book.updateBookStatus(bookStatus);
	}
}
