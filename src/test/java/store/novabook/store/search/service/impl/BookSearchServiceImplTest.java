package store.novabook.store.search.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.InternalServerException;
import store.novabook.store.search.document.BookDocument;
import store.novabook.store.search.dto.GetBookSearchResponse;
import store.novabook.store.search.repository.BookSearchRepository;

@ExtendWith(MockitoExtension.class)
class BookSearchServiceImplTest {

	@Mock
	private BookSearchRepository bookSearchRepository;

	@InjectMocks
	private BookSearchServiceImpl bookSearchService;

	private Pageable pageable;
	private BookDocument bookDocument;
	private Page<BookDocument> bookDocumentPage;

	@BeforeEach
	void setUp() {
		pageable = PageRequest.of(0, 10);
		BookDocument bookDocument = BookDocument.builder()
			.id(1L)
			.title("Sample Book Title")
			.author("Sample Author")
			.publisher("Sample Publisher")
			.image("http://example.com/sample.jpg")
			.price(2000L)
			.discountPrice(1500L)
			.score(4.5)
			.isPackaged(false)
			.review(10)
			.tagList(Arrays.asList("Tag1", "Tag2"))
			.categoryList(Arrays.asList("Category1", "Category2"))
			.build();
		bookDocumentPage = new PageImpl<>(Collections.singletonList(bookDocument));
	}

	@Test
	void searchByKeywordContaining() {
		String keyword = "test";
		when(bookSearchRepository.findAllByKeywordIgnoreCase(keyword, pageable)).thenReturn(bookDocumentPage);

		Page<GetBookSearchResponse> result = bookSearchService.searchByKeywordContaining(keyword, pageable);

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		verify(bookSearchRepository).findAllByKeywordIgnoreCase(keyword, pageable);
	}

	@Test
	void searchByKeywordContaining_throwsInternalServerException() {
		String keyword = "test";
		when(bookSearchRepository.findAllByKeywordIgnoreCase(keyword, pageable)).thenThrow(new RuntimeException());

		InternalServerException exception = assertThrows(InternalServerException.class, () -> {
			bookSearchService.searchByKeywordContaining(keyword, pageable);
		});

		assertEquals(ErrorCode.INVALID_REQUEST_ARGUMENT, exception.getErrorCode());
		verify(bookSearchRepository).findAllByKeywordIgnoreCase(keyword, pageable);
	}

	@Test
	void searchByAuthorContaining() {
		String author = "author";
		when(bookSearchRepository.findAllByAuthorIgnoreCase(author, pageable)).thenReturn(bookDocumentPage);

		Page<GetBookSearchResponse> result = bookSearchService.searchByAuthorContaining(author, pageable);

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		verify(bookSearchRepository).findAllByAuthorIgnoreCase(author, pageable);
	}

	@Test
	void searchByAuthorContaining_throwsInternalServerException() {
		String author = "author";
		when(bookSearchRepository.findAllByAuthorIgnoreCase(author, pageable)).thenThrow(new RuntimeException());

		InternalServerException exception = assertThrows(InternalServerException.class, () -> {
			bookSearchService.searchByAuthorContaining(author, pageable);
		});

		assertEquals(ErrorCode.INVALID_REQUEST_ARGUMENT, exception.getErrorCode());
		verify(bookSearchRepository).findAllByAuthorIgnoreCase(author, pageable);
	}

	@Test
	void searchByPublishContaining() {
		String publisher = "publisher";
		when(bookSearchRepository.findAllByPublishIgnoreCase(publisher, pageable)).thenReturn(bookDocumentPage);

		Page<GetBookSearchResponse> result = bookSearchService.searchByPublishContaining(publisher, pageable);

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		verify(bookSearchRepository).findAllByPublishIgnoreCase(publisher, pageable);
	}

	@Test
	void searchByPublishContaining_throwsInternalServerException() {
		String publisher = "publisher";
		when(bookSearchRepository.findAllByPublishIgnoreCase(publisher, pageable)).thenThrow(new RuntimeException());

		InternalServerException exception = assertThrows(InternalServerException.class, () -> {
			bookSearchService.searchByPublishContaining(publisher, pageable);
		});

		assertEquals(ErrorCode.INVALID_REQUEST_ARGUMENT, exception.getErrorCode());
		verify(bookSearchRepository).findAllByPublishIgnoreCase(publisher, pageable);
	}

	@Test
	void searchByCategoryListContaining() {
		String category = "category";
		when(bookSearchRepository.findAllByCategoryListMatches(category, pageable)).thenReturn(bookDocumentPage);

		Page<GetBookSearchResponse> result = bookSearchService.searchByCategoryListContaining(category, pageable);

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		verify(bookSearchRepository).findAllByCategoryListMatches(category, pageable);
	}

	@Test
	void searchByCategoryListContaining_throwsInternalServerException() {
		String category = "category";
		when(bookSearchRepository.findAllByCategoryListMatches(category, pageable)).thenThrow(new RuntimeException());

		InternalServerException exception = assertThrows(InternalServerException.class, () -> {
			bookSearchService.searchByCategoryListContaining(category, pageable);
		});

		assertEquals(ErrorCode.INVALID_REQUEST_ARGUMENT, exception.getErrorCode());
		verify(bookSearchRepository).findAllByCategoryListMatches(category, pageable);
	}
}