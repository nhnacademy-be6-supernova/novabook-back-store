package store.novabook.store.category.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import store.novabook.store.book.entity.Book;
import store.novabook.store.category.dto.request.CreateCategoryRequest;
import store.novabook.store.category.dto.response.CreateCategoryResponse;
import store.novabook.store.category.dto.response.DeleteResponse;
import store.novabook.store.category.dto.response.GetCategoryIdsByBookIdResponse;
import store.novabook.store.category.dto.response.GetCategoryListResponse;
import store.novabook.store.category.dto.response.GetCategoryResponse;
import store.novabook.store.category.entity.BookCategory;
import store.novabook.store.category.entity.Category;
import store.novabook.store.category.repository.BookCategoryRepository;
import store.novabook.store.category.repository.CategoryRepository;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CategoryServiceImplTest {

	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private BookCategoryRepository bookCategoryRepository;

	@Mock
	private CacheManager cacheManager;

	@InjectMocks
	private CategoryServiceImpl categoryService;

	private Cache cache;

	@BeforeEach
	void setUp() {
		cache = new ConcurrentMapCacheManager().getCache("categories");
		when(cacheManager.getCache("categories")).thenReturn(cache);
		when(cacheManager.getCache("getCategoryListResponse")).thenReturn(cache);
		when(cacheManager.getCache("categoryCache")).thenReturn(cache);
	}

	@Test
	void testCreateCategoryWithoutTopCategory() {
		CreateCategoryRequest request = new CreateCategoryRequest(null, "Category");
		Category category = new Category("Category");
		when(categoryRepository.save(any(Category.class))).thenReturn(category);

		CreateCategoryResponse response = categoryService.create(request);

		assertNotNull(response);
		assertEquals("Category", category.getName());
		verify(categoryRepository, times(1)).save(any(Category.class));
	}

	@Test
	void testCreateCategoryWithTopCategory() {
		Category topCategory = spy(new Category("TopCategory"));
		doReturn(new ArrayList<>()).when(topCategory).getSubCategories();

		CreateCategoryRequest request = new CreateCategoryRequest(1L, "Category");
		when(categoryRepository.findById(1L)).thenReturn(Optional.of(topCategory));
		when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
			Category category = invocation.getArgument(0);
			doReturn(Collections.singletonList(category)).when(topCategory).getSubCategories();
			return category;
		});

		CreateCategoryResponse response = categoryService.create(request);

		assertNotNull(response);
		assertEquals("Category", topCategory.getSubCategories().get(0).getName());
		verify(categoryRepository, times(1)).save(any(Category.class));
		verify(categoryRepository, times(1)).findById(1L);
	}

	@Test
	void testCreateCategoryWithNonExistingTopCategory() {
		CreateCategoryRequest request = new CreateCategoryRequest(1L, "Category");
		when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class, () -> categoryService.create(request));

		assertEquals(ErrorCode.CATEGORY_NOT_FOUND, exception.getErrorCode());
		verify(categoryRepository, times(1)).findById(1L);
	}

	@Test
	void testGetCategory() {
		Category category = new Category("Category");
		when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

		GetCategoryResponse response = categoryService.getCategory(1L);

		assertNotNull(response);
		assertEquals("Category", response.name());
		verify(categoryRepository, times(1)).findById(1L);
	}

	@Test
	void testGetCategoryNotFound() {
		when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class, () -> categoryService.getCategory(1L));

		assertEquals(ErrorCode.CATEGORY_NOT_FOUND, exception.getErrorCode());
		verify(categoryRepository, times(1)).findById(1L);
	}

	@Test
	void testGetAllCategories() {
		Category category = new Category("Category");
		when(categoryRepository.findAllByOrderByTopCategoryDesc()).thenReturn(Collections.singletonList(category));

		GetCategoryListResponse response = categoryService.getAllCategories();

		assertNotNull(response);
		assertEquals(1, response.categories().size());
		verify(categoryRepository, times(1)).findAllByOrderByTopCategoryDesc();
	}

	@Test
	void testConvertToDTO() {
		Category category = spy(new Category("Category"));
		doReturn(new ArrayList<>()).when(category).getSubCategories();
		Category subCategory = new Category(category, "SubCategory");
		category.getSubCategories().add(subCategory);

		GetCategoryResponse response = categoryService.convertToDTO(category);

		assertNotNull(response);
		assertEquals("Category", response.name());
		assertEquals(1, response.sub().size());
		assertEquals("SubCategory", response.sub().get(0).name());
	}

	@Test
	void testDeleteCategoryWithBookCategory() {
		when(bookCategoryRepository.existsByCategoryId(1L)).thenReturn(true);

		DeleteResponse response = categoryService.delete(1L);

		assertFalse(response.isDeleted());
		verify(bookCategoryRepository, times(1)).existsByCategoryId(1L);
		verify(categoryRepository, never()).deleteById(1L);
		verify(cacheManager, times(2)).getCache(anyString());
	}

	@Test
	void testDeleteCategoryWithoutBookCategory() {
		when(bookCategoryRepository.existsByCategoryId(1L)).thenReturn(false);

		DeleteResponse response = categoryService.delete(1L);

		assertTrue(response.isDeleted());
		verify(bookCategoryRepository, times(1)).existsByCategoryId(1L);
		verify(categoryRepository, times(1)).deleteById(1L);
		verify(cacheManager, times(2)).getCache(anyString());
	}

	@Test
	void testGetCategoryIdsByBookId() {
		Book book = mock(Book.class);
		Category category = mock(Category.class);
		BookCategory bookCategory = spy(BookCategory.builder().book(book).category(category).build());
		doReturn(1L).when(bookCategory).getId();

		when(bookCategoryRepository.findAllByBookId(1L)).thenReturn(Collections.singletonList(bookCategory));

		GetCategoryIdsByBookIdResponse response = categoryService.getCategoryIdsByBookId(1L);

		assertNotNull(response);
		assertEquals(1, response.categoryIds().size());
		assertEquals(1L, response.categoryIds().get(0));
		verify(bookCategoryRepository, times(1)).findAllByBookId(1L);
	}
}
