package store.novabook.store.book.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import store.novabook.store.book.dto.response.GetLikeBookResponse;
import store.novabook.store.book.dto.response.LikeBookResponse;
import store.novabook.store.book.service.LikesService;

@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
@AutoConfigureMockMvc
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(LikesController.class)
class LikesControllerTest {

	@MockBean
	private LikesService likesService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}


	@Test
	void testGetLikes() throws Exception {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
		Page<GetLikeBookResponse> page = new PageImpl<>(Collections.singletonList(
			GetLikeBookResponse.builder()
				.id(1L)
				.bookId(1L)
				.title("Book Title")
				.author("Author Name")
				.publisher("Publisher Name")
				.build()
		));

		when(likesService.myLikes(any(), eq(pageable))).thenReturn(page);

		mockMvc.perform(get("/api/v1/store/books/likes/member")
				.requestAttr("memberId", 1L)
				.param("page", "0")
				.param("size", "10")
				.param("sort", "id,desc"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data[0].id").value(1L))
			.andExpect(jsonPath("$.data[0].bookId").value(1L))
			.andExpect(jsonPath("$.data[0].title").value("Book Title"))
			.andExpect(jsonPath("$.data[0].author").value("Author Name"))
			.andExpect(jsonPath("$.data[0].publisher").value("Publisher Name"));
	}

	@Test
	void testLikeButton() throws Exception {
		LikeBookResponse response = LikeBookResponse.builder()
			.isLiked(true)
			.build();

		when(likesService.likeButton(any(), anyLong())).thenReturn(response);

		mockMvc.perform(post("/api/v1/store/books/likes/1")
				.with(csrf())
				.requestAttr("memberId", 1L))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.body.isLiked").value(true));
	}

	@Test
	void testIsLiked() throws Exception {
		LikeBookResponse response = LikeBookResponse.builder()
			.isLiked(true)
			.build();

		when(likesService.getLikeResponse(any(), anyLong())).thenReturn(response);

		mockMvc.perform(get("/api/v1/store/books/likes/1")
				.requestAttr("memberId", 1L))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.isLiked").value(true));
	}
}
