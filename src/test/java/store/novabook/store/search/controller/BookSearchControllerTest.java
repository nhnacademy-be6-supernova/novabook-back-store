package store.novabook.store.search.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import store.novabook.store.search.dto.GetBookSearchResponse;
import store.novabook.store.search.service.impl.BookSearchServiceImpl;

@WithMockUser
@AutoConfigureMockMvc
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(BookSearchController.class)
class BookSearchControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookSearchServiceImpl bookSearchService;

	private Page<GetBookSearchResponse> responsePage;

	@BeforeEach
	void setUp() {
		GetBookSearchResponse response = new GetBookSearchResponse(1L, "Test Book", "Test Author", "Test Publisher",
			"http://test.com/image.jpg", 1000L, 900L, 4.5, false, 10);
		responsePage = new PageImpl<>(Collections.singletonList(response), PageRequest.of(0, 10), 1);
	}

	@Test
	void searchByKeyword() throws Exception {
		given(bookSearchService.searchByKeywordContaining(eq("Test"), any(Pageable.class))).willReturn(responsePage);

		mockMvc.perform(get("/api/v1/store/search/keyword")
				.param("title", "Test")
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data[0].title").value("Test Book"));
	}

	@Test
	void searchByAuthor() throws Exception {
		given(bookSearchService.searchByAuthorContaining(eq("Test Author"), any(Pageable.class))).willReturn(responsePage);

		mockMvc.perform(get("/api/v1/store/search/author")
				.param("author", "Test Author")
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data[0].author").value("Test Author"));
	}

	@Test
	void searchByPublish() throws Exception {
		given(bookSearchService.searchByPublishContaining(eq("Test Publisher"), any(Pageable.class))).willReturn(responsePage);

		mockMvc.perform(get("/api/v1/store/search/publish")
				.param("publish", "Test Publisher")
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data[0].publisher").value("Test Publisher"));
	}

	@Test
	void searchByCategory() throws Exception {
		given(bookSearchService.searchByCategoryListContaining(eq("Test Category"), any(Pageable.class))).willReturn(responsePage);

		mockMvc.perform(get("/api/v1/store/search/category")
				.param("category", "Test Category")
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data[0].title").value("Test Book"));
	}
}