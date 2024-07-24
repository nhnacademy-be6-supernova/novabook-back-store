package store.novabook.store.book.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import store.novabook.store.book.dto.request.CreateBookRequest;
import store.novabook.store.book.dto.request.UpdateBookRequest;
import store.novabook.store.book.dto.response.CreateBookResponse;
import store.novabook.store.book.dto.response.GetBookAllResponse;
import store.novabook.store.book.dto.response.GetBookResponse;
import store.novabook.store.book.dto.response.GetBookToMainResponseMap;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.BookStatus;
import store.novabook.store.book.repository.BookQueryRepository;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.book.repository.BookStatusRepository;
import store.novabook.store.category.entity.Category;
import store.novabook.store.category.repository.BookCategoryRepository;
import store.novabook.store.category.repository.CategoryRepository;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.image.service.ImageService;
import store.novabook.store.tag.entity.Tag;
import store.novabook.store.tag.repository.BookTagRepository;
import store.novabook.store.tag.repository.TagRepository;

class BookServiceImplTest {

	@InjectMocks
	private BookServiceImpl bookService;

	@Mock
	private BookRepository bookRepository;

	@Mock
	private BookStatusRepository bookStatusRepository;

	@Mock
	private BookTagRepository bookTagRepository;

	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private TagRepository tagRepository;

	@Mock
	private BookCategoryRepository bookCategoryRepository;

	@Mock
	private BookQueryRepository queryRepository;

	@Mock
	private ImageService imageService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateBook() {
		CreateBookRequest request = new CreateBookRequest(
			1L, "1234567890", "Test Book", "Description",
			"Detail Description", "Author", "Publisher",
			LocalDateTime.now(), 100, 200L, 150L, true,
			"image.jpg", List.of(1L, 2L), List.of(1L, 2L)
		);

		BookStatus bookStatus = new BookStatus(1L, "Available", LocalDateTime.now(), LocalDateTime.now());

		when(bookStatusRepository.findById(request.bookStatusId())).thenReturn(Optional.of(bookStatus));
		when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.<Book>getArgument(0));

		List<Tag> tags = List.of(new Tag("Tag1"), new Tag("Tag2"));
		when(tagRepository.findByIdIn(request.tags())).thenReturn(tags);

		List<Category> categories = List.of(new Category("Category1"), new Category("Category2"));
		when(categoryRepository.findByIdIn(request.categories())).thenReturn(categories);

		CreateBookResponse response = bookService.create(request);

		assertThat(response).isNotNull();

		verify(bookRepository).save(any(Book.class));
		verify(bookTagRepository).saveAll(anyList());
		verify(bookCategoryRepository).saveAll(anyList());
		verify(imageService).createBookImage(any(Book.class), eq("image.jpg"));
	}

	@Test
	void testGetBook() {
		Long bookId = 1L;
		GetBookResponse getBookResponse = mock(GetBookResponse.class);
		when(queryRepository.getBook(bookId)).thenReturn(getBookResponse);

		GetBookResponse response = bookService.getBook(bookId);

		assertThat(response).isEqualTo(getBookResponse);
	}

	@Test
	void testGetBookAll() {
		// Pageable 객체 생성 (페이지 번호: 0, 페이지 크기: 10, 정렬: createdAt 내림차순)
		Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
		Pageable pageable = PageRequest.of(0, 10, sort);

		// Book 객체를 모킹하고 페이지 객체를 생성
		Book book = mock(Book.class);
		Page<Book> bookPage = new PageImpl<>(Collections.singletonList(book));

		// bookRepository.findAll 메서드가 pageable을 인자로 받을 때 bookPage를 반환하도록 설정
		when(bookRepository.findAll(eq(pageable))).thenReturn(bookPage);

		// 서비스 메서드 호출
		Page<GetBookAllResponse> response = bookService.getBookAll(pageable);

		// 응답 검증
		assertThat(response).isNotNull();
		assertThat(response.getTotalElements()).isEqualTo(1);
	}

	@Test
	void testUpdateBook() {
		UpdateBookRequest request = UpdateBookRequest.builder()
			.id(1L).bookStatusId(1L).inventory(100)
			.price(200L).discountPrice(150L).isPackaged(true)
			.build();

		BookStatus bookStatus = new BookStatus(1L, "Available", LocalDateTime.now(), LocalDateTime.now());
		Book book = mock(Book.class);

		when(bookStatusRepository.findById(request.bookStatusId())).thenReturn(Optional.of(bookStatus));
		when(bookRepository.findById(request.id())).thenReturn(Optional.of(book));

		bookService.update(request);

		verify(book).update(bookStatus, request);
	}

	@Test
	void testDeleteBook() {
		Long bookId = 1L;
		BookStatus bookStatus = new BookStatus(4L, "Deleted", LocalDateTime.now(), LocalDateTime.now());
		Book book = mock(Book.class);

		when(bookStatusRepository.findById(4L)).thenReturn(Optional.of(bookStatus));
		when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

		bookService.delete(bookId);

		verify(book).updateBookStatus(bookStatus);
	}

	@Test
	void testGetBookToMainPage() {
		GetBookToMainResponseMap getBookToMainResponseMap = mock(GetBookToMainResponseMap.class);
		when(queryRepository.getBookToMainPage()).thenReturn(getBookToMainResponseMap);

		GetBookToMainResponseMap response = bookService.getBookToMainPage();

		assertThat(response).isEqualTo(getBookToMainResponseMap);
	}

	@Test
	void testCreateBookStatusNotFound() {
		CreateBookRequest request = new CreateBookRequest(
			1L, "1234567890", "Test Book", "Description",
			"Detail Description", "Author", "Publisher",
			LocalDateTime.now(), 100, 200L, 150L, true,
			"image.jpg", List.of(1L, 2L), List.of(1L, 2L)
		);

		when(bookStatusRepository.findById(request.bookStatusId())).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class, () -> bookService.create(request));

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.BOOK_STATUS_NOT_FOUND);
	}

	@Test
	void testUpdateBookNotFound() {
		BookStatus bookStatus = new BookStatus(1L, "Available", LocalDateTime.now(), LocalDateTime.now());
		UpdateBookRequest request = UpdateBookRequest.builder()
			.id(1L).bookStatusId(1L).inventory(100)
			.price(200L).discountPrice(150L).isPackaged(true)
			.build();

		when(bookRepository.findById(request.id())).thenReturn(Optional.empty());
		when(bookStatusRepository.findById(request.id())).thenReturn(Optional.of(bookStatus));

		NotFoundException exception = assertThrows(NotFoundException.class, () -> bookService.update(request));

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.BOOK_NOT_FOUND);
	}
}
