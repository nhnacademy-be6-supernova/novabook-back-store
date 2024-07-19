package store.novabook.store.category.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

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

@WithMockUser
@AutoConfigureMockMvc
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CategoryService categoryService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateCategory() throws Exception {
		CreateCategoryRequest request = new CreateCategoryRequest(null, "New Category");
		CreateCategoryResponse response = new CreateCategoryResponse(1L);

		when(categoryService.create(any(CreateCategoryRequest.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/store/categories").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(request))
				.with(csrf()))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.body.id").value(1L))
			.andExpect(jsonPath("$.header.isSuccessful").value(true))
			.andExpect(jsonPath("$.header.resultMessage").value("SUCCESS"));

		verify(categoryService, times(1)).create(any(CreateCategoryRequest.class));
	}

	@Test
	void testGetCategory() throws Exception {
		GetCategoryResponse response = new GetCategoryResponse(1L, "Category", Collections.emptyList());

		when(categoryService.getCategory(1L)).thenReturn(response);

		mockMvc.perform(get("/api/v1/store/categories/1").with(csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.id").value(1L))
			.andExpect(jsonPath("$.body.name").value("Category"))
			.andExpect(jsonPath("$.header.isSuccessful").value(true))
			.andExpect(jsonPath("$.header.resultMessage").value("SUCCESS"));

		verify(categoryService, times(1)).getCategory(1L);
	}

	@Test
	void testGetCategoryAll() throws Exception {
		GetCategoryListResponse response = GetCategoryListResponse.builder()
			.categories(Collections.singletonList(new GetCategoryResponse(1L, "Category", Collections.emptyList())))
			.build();

		when(categoryService.getAllCategories()).thenReturn(response);

		mockMvc.perform(get("/api/v1/store/categories").with(csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.categories[0].id").value(1L))
			.andExpect(jsonPath("$.body.categories[0].name").value("Category"))
			.andExpect(jsonPath("$.header.isSuccessful").value(true))
			.andExpect(jsonPath("$.header.resultMessage").value("SUCCESS"));

		verify(categoryService, times(1)).getAllCategories();
	}

	@Test
	void testGetCategoryByBId() throws Exception {
		GetCategoryIdsByBookIdResponse response = new GetCategoryIdsByBookIdResponse(Collections.singletonList(1L));

		when(categoryService.getCategoryIdsByBookId(1L)).thenReturn(response);

		mockMvc.perform(get("/api/v1/store/categories/book/1").with(csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.categoryIds[0]").value(1L))
			.andExpect(jsonPath("$.header.isSuccessful").value(true))
			.andExpect(jsonPath("$.header.resultMessage").value("SUCCESS"));

		verify(categoryService, times(1)).getCategoryIdsByBookId(1L);
	}

	@Test
	void testDeleteCategory() throws Exception {
		DeleteResponse response = new DeleteResponse(true);

		when(categoryService.delete(1L)).thenReturn(response);

		mockMvc.perform(delete("/api/v1/store/categories/1").with(csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.isDeleted").value(true))
			.andExpect(jsonPath("$.header.isSuccessful").value(true))
			.andExpect(jsonPath("$.header.resultMessage").value("SUCCESS"));

		verify(categoryService, times(1)).delete(1L);
	}
}
