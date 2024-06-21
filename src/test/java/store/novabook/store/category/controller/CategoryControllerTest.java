package store.novabook.store.category.controller;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import store.novabook.store.category.dto.CreateCategoryRequest;
import store.novabook.store.category.dto.CreateCategoryResponse;
import store.novabook.store.category.entity.GetCategoryResponse;
import store.novabook.store.category.service.CategoryService;

@WebMvcTest(CategoryController.class)
@ContextConfiguration(classes = {CategoryService.class})
@EnableSpringDataWebSupport
class CategoryControllerTest {
	@Autowired
	protected MockMvc mockMvc;

	@MockBean
	private CategoryService categoryService;

	private ObjectMapper objectMapper;

	@BeforeEach
	public void setup() {
		CategoryController categoryController = new CategoryController(categoryService);
		objectMapper = new ObjectMapper();
		PageableHandlerMethodArgumentResolver pageableArgumentResolver = new PageableHandlerMethodArgumentResolver();
		mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
			.setCustomArgumentResolvers(pageableArgumentResolver)
			.build();
	}
	@Test
	void createCategory() throws Exception {

		CreateCategoryRequest request = new CreateCategoryRequest("카테고리1");
		CreateCategoryResponse response = new CreateCategoryResponse(1L);
		when(categoryService.create(any(CreateCategoryRequest.class))).thenReturn(response);

		mockMvc.perform(post("/categories")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(1L));
	}

	@Test
	void getCategory() throws Exception {
		GetCategoryResponse response = new GetCategoryResponse(1L,"cate");
		when(categoryService.getCategory(1L)).thenReturn(response);

		mockMvc.perform(get("/categories/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1L))
			.andExpect(jsonPath("$.name").value("cate"));
	}

	@Test
	void getCategoryAll() throws Exception {
		GetCategoryResponse response = new GetCategoryResponse(1L,"cate");
		Pageable pageable = PageRequest.of(0, 10);
		List<GetCategoryResponse> responses = Collections.singletonList(response);
		Page<GetCategoryResponse> page = new PageImpl<>(responses, pageable, 1);

		when(categoryService.getCategoryAll(pageable)).thenReturn(page);

		mockMvc.perform(get("/categories")
				.contentType(MediaType.APPLICATION_JSON)
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].id").value(1L))
			.andExpect(jsonPath("$.content[0].name").value("cate"));
	}

	@Test
	void deleteCategory() throws Exception {
		doNothing().when(categoryService).delete(1L);

		mockMvc.perform(delete("/categories/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
}