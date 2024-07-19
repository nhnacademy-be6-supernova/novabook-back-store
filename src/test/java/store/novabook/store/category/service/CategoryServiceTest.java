package store.novabook.store.category.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

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
import store.novabook.store.category.service.impl.CategoryServiceImpl;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private BookCategoryRepository bookCategoryRepository;

	@Mock
	private CacheManager cacheManager;

	@InjectMocks
	private CategoryServiceImpl categoryService;

	private Cache categoryCache;
	private Cache categoryListCache;

	@BeforeEach
	void setUp() {
		categoryCache = mock(Cache.class);
		categoryListCache = mock(Cache.class);
		lenient().when(cacheManager.getCache("categoryCache")).thenReturn(categoryCache);
		lenient().when(cacheManager.getCache("getCategoryListResponse")).thenReturn(categoryListCache);
	}

	@Test
	void deleteCategory_withBook() {
		// given
		Long categoryId = 1L;
		when(bookCategoryRepository.existsByCategoryId(categoryId)).thenReturn(true);

		// when
		DeleteResponse response = categoryService.delete(categoryId);

		// then
		assertThat(response.isDeleted()).isFalse();
		verify(bookCategoryRepository, times(1)).existsByCategoryId(categoryId);
		verify(categoryRepository, times(0)).deleteById(categoryId);
		verify(categoryCache, times(1)).evict(categoryId);
		verify(categoryListCache, times(1)).evict(categoryId);
	}

	@Test
	void deleteCategory_withoutBook() {
		// given
		Long categoryId = 1L;
		when(bookCategoryRepository.existsByCategoryId(categoryId)).thenReturn(false);

		// when
		DeleteResponse response = categoryService.delete(categoryId);

		// then
		assertThat(response.isDeleted()).isTrue();
		verify(bookCategoryRepository, times(1)).existsByCategoryId(categoryId);
		verify(categoryRepository, times(1)).deleteById(categoryId);
		verify(categoryCache, times(1)).evict(categoryId);
		verify(categoryListCache, times(1)).evict(categoryId);
	}

	@Test
	void createCategory() {
		// given
		CreateCategoryRequest request = new CreateCategoryRequest(null, "New Category");
		Category category = Mockito.spy(new Category(request.name()));
		doReturn(1L).when(category).getId();
		when(categoryRepository.save(any(Category.class))).thenReturn(category);

		// when
		CreateCategoryResponse response = categoryService.create(request);

		// then
		assertThat(response.id()).isEqualTo(1L);
		verify(categoryRepository, times(1)).save(any(Category.class));
	}

	@Test
	void getCategory() {
		// given
		Long categoryId = 1L;
		Category category = Mockito.spy(new Category("Category1"));
		doReturn(categoryId).when(category).getId();
		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

		// when
		GetCategoryResponse response = categoryService.getCategory(categoryId);

		// then
		assertThat(response.id()).isEqualTo(categoryId);
		assertThat(response.name()).isEqualTo("Category1");
		verify(categoryRepository, times(1)).findById(categoryId);
	}

	@Test
	void getCategory_notFound() {
		// given
		Long categoryId = 1L;
		when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

		// when, then
		NotFoundException exception = assertThrows(NotFoundException.class, () -> {
			categoryService.getCategory(categoryId);
		});

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CATEGORY_NOT_FOUND);
	}

	@Test
	void getAllCategories() {
		// given
		Category category1 = Mockito.spy(new Category("Category1"));
		doReturn(1L).when(category1).getId();
		Category category2 = Mockito.spy(new Category("Category2"));
		doReturn(2L).when(category2).getId();
		when(categoryRepository.findAllByOrderByTopCategoryDesc()).thenReturn(List.of(category1, category2));

		// when
		GetCategoryListResponse response = categoryService.getAllCategories();

		// then
		assertThat(response.categories()).hasSize(2);
		assertThat(response.categories().get(0).id()).isEqualTo(1L);
		assertThat(response.categories().get(1).id()).isEqualTo(2L);
		verify(categoryRepository, times(1)).findAllByOrderByTopCategoryDesc();
	}

	@Test
	void getCategoryIdsByBookId() {
		// given
		Long bookId = 1L;
		BookCategory bookCategory1 = Mockito.spy(BookCategory.of(null, new Category("Category1")));
		doReturn(1L).when(bookCategory1).getId();
		BookCategory bookCategory2 = Mockito.spy(BookCategory.of(null, new Category("Category2")));
		doReturn(2L).when(bookCategory2).getId();
		when(bookCategoryRepository.findAllByBookId(bookId)).thenReturn(List.of(bookCategory1, bookCategory2));

		// when
		GetCategoryIdsByBookIdResponse response = categoryService.getCategoryIdsByBookId(bookId);

		// then
		assertThat(response.categoryIds()).containsExactlyInAnyOrder(1L, 2L);
		verify(bookCategoryRepository, times(1)).findAllByBookId(bookId);
	}
}
