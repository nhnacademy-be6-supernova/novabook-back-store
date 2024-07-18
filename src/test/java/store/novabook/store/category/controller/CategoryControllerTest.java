package store.novabook.store.category.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import store.novabook.store.category.dto.request.CreateCategoryRequest;
import store.novabook.store.category.dto.response.CreateCategoryResponse;
import store.novabook.store.category.dto.response.DeleteResponse;
import store.novabook.store.category.dto.response.GetCategoryIdsByBookIdResponse;
import store.novabook.store.category.dto.response.GetCategoryListResponse;
import store.novabook.store.category.dto.response.GetCategoryResponse;
import store.novabook.store.category.service.CategoryService;

@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
@WebMvcTest(CategoryController.class)
@MockBean(JpaMetamodelMappingContext.class)
class CategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CategoryService categoryService;

	@BeforeEach
	void setUp() {MockitoAnnotations.openMocks(this);}

	@Test
	void createCategory() throws Exception {
		// given
		CreateCategoryRequest createCategoryRequest = new CreateCategoryRequest(null, "New Category");
		CreateCategoryResponse createCategoryResponse = new CreateCategoryResponse(1L);
		when(categoryService.create(any(CreateCategoryRequest.class))).thenReturn(createCategoryResponse);

		// when, then
		mockMvc.perform(post("/api/v1/store/categories")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(createCategoryRequest)))
			.andExpect(status().isCreated())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.body.id").value(1));

		verify(categoryService, times(1)).create(any(CreateCategoryRequest.class));
	}

	@Test
	void getCategory() throws Exception {
		// given
		Long categoryId = 1L;
		GetCategoryResponse getCategoryResponse = new GetCategoryResponse(categoryId, "Category1", List.of());
		when(categoryService.getCategory(categoryId)).thenReturn(getCategoryResponse);

		// when, then
		mockMvc.perform(get("/api/v1/store/categories/{id}", categoryId))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.body.id").value(1))
			.andExpect(jsonPath("$.body.name").value("Category1"))
			.andExpect(jsonPath("$.body.sub").isEmpty());

		verify(categoryService, times(1)).getCategory(categoryId);
	}

	@Test
	void getCategoryAll() throws Exception {
		// given
		List<GetCategoryResponse> categoryResponses = List.of(
			new GetCategoryResponse(1L, "Category1", List.of()),
			new GetCategoryResponse(2L, "Category2", List.of())
		);
		GetCategoryListResponse getCategoryListResponse = new GetCategoryListResponse(categoryResponses);
		when(categoryService.getAllCategories()).thenReturn(getCategoryListResponse);

		// when, then
		mockMvc.perform(get("/api/v1/store/categories"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.body.categories[0].id").value(1))
			.andExpect(jsonPath("$.body.categories[0].name").value("Category1"))
			.andExpect(jsonPath("$.body.categories[1].id").value(2))
			.andExpect(jsonPath("$.body.categories[1].name").value("Category2"));

		verify(categoryService, times(1)).getAllCategories();
	}

	@Test
	void getCategoryByBId() throws Exception {
		// given
		Long bookId = 1L;
		List<Long> categoryIds = List.of(1L, 2L);
		GetCategoryIdsByBookIdResponse response = new GetCategoryIdsByBookIdResponse(categoryIds);
		when(categoryService.getCategoryIdsByBookId(bookId)).thenReturn(response);

		// when, then
		mockMvc.perform(get("/api/v1/store/categories/book/{bookId}", bookId))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.body.categoryIds[0]").value(1))
			.andExpect(jsonPath("$.body.categoryIds[1]").value(2));

		verify(categoryService, times(1)).getCategoryIdsByBookId(bookId);
	}

	@Test
	void deleteCategory() throws Exception {
		// given
		Long categoryId = 1L;
		DeleteResponse deleteResponse = new DeleteResponse(true);
		when(categoryService.delete(categoryId)).thenReturn(deleteResponse);

		// when, then
		mockMvc.perform(delete("/api/v1/store/categories/{id}", categoryId)
				.with(csrf()))

			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.body.isDeleted").value(true));

		verify(categoryService, times(1)).delete(categoryId);
	}
}