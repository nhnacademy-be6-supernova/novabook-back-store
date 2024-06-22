// package store.novabook.store.category.service;
//
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.Mockito.anyLong;
// import static org.mockito.Mockito.*;
//
// import java.util.Collections;
// import java.util.List;
// import java.util.Optional;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Pageable;
//
// import store.novabook.store.category.dto.CreateCategoryRequest;
// import store.novabook.store.category.dto.CreateCategoryResponse;
// import store.novabook.store.category.entity.Category;
// import store.novabook.store.category.dto.GetCategoryListResponse;
// import store.novabook.store.category.repository.CategoryRepository;
//
// @ExtendWith(MockitoExtension.class)
// class CategoryServiceTest {
// 	@Mock
// 	private CategoryRepository categoryRepository;
// 	@InjectMocks
// 	private CategoryService categoryService;
//
// 	private Category category;
// 	private CreateCategoryRequest createCategoryRequest;
//
// 	@BeforeEach
// 	void setUp() {
// 		createCategoryRequest= new CreateCategoryRequest("cate");
// 		category = new Category(createCategoryRequest.name());
// 	}
//
// 	@Test
// 	void create() {
// 		when(categoryRepository.save(any(Category.class))).thenReturn(category);
// 		CreateCategoryResponse createCategoryResponse = categoryService.create(createCategoryRequest);
// 		assertNotNull(createCategoryResponse);
// 		assertEquals(category.getId(), createCategoryResponse.id());
// 		verify(categoryRepository, times(1)).save(any(Category.class));
// 	}
//
// 	@Test
// 	void getCategory() {
// 		when(categoryRepository.findById(1L)).thenReturn(Optional.ofNullable(category));
// 		GetCategoryListResponse getCategoryResponse = categoryService.getCategory(1L);
// 		assertEquals(category.getId(), getCategoryResponse.id());
// 		verify(categoryRepository, times(1)).findById(anyLong());
// 	}
//
// 	@Test
// 	void getCategoryAll() {
// 		List<Category> categories = Collections.singletonList(category);
// 		Page<Category> page = new PageImpl<>(categories, PageRequest.of(0, 10), categories.size());
//
// 		when( categoryRepository.findAll(any(Pageable.class))).thenReturn(page);
//
// 		Page<GetCategoryListResponse> result = categoryService.getCategoryAll(PageRequest.of(0, 10));
//
// 		assertNotNull(result);
// 		assertEquals(1, result.getTotalElements());
// 	}
//
// 	@Test
// 	void delete() {
// 		categoryService.delete(1L);
// 		verify(categoryRepository, times(1)).deleteById(anyLong());
// 	}
// }