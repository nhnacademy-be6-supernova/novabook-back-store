package store.novabook.store.book.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import store.novabook.store.book.dto.request.CreateReviewRequest;
import store.novabook.store.book.dto.request.UpdateReviewRequest;
import store.novabook.store.book.dto.response.CreateReviewResponse;
import store.novabook.store.book.dto.response.GetReviewListResponse;
import store.novabook.store.book.dto.response.GetReviewResponse;
import store.novabook.store.book.service.ReviewService;

@WebMvcTest(ReviewController.class)
@AutoConfigureMockMvc
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
class ReviewControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ReviewService reviewService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetReview() throws Exception {
		GetReviewResponse response = GetReviewResponse.builder()
			.nickName("John")
			.reviewId(1L)
			.orderBookId(1L)
			.content("Great book!")
			.reviewImages(Collections.singletonList("image.jpg"))
			.score(5)
			.createdAt(LocalDateTime.now())
			.build();

		when(reviewService.getReviewById(1L)).thenReturn(response);

		mockMvc.perform(get("/api/v1/store/reviews/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.nickName").value("John"))
			.andExpect(jsonPath("$.body.reviewId").value(1L))
			.andExpect(jsonPath("$.body.orderBookId").value(1L))
			.andExpect(jsonPath("$.body.content").value("Great book!"))
			.andExpect(jsonPath("$.body.reviewImages[0]").value("image.jpg"))
			.andExpect(jsonPath("$.body.score").value(5));
	}

	@Test
	void testGetReviewByBookId() throws Exception {
		GetReviewResponse reviewResponse = GetReviewResponse.builder()
			.nickName("John")
			.reviewId(1L)
			.orderBookId(1L)
			.content("Great book!")
			.reviewImages(Collections.singletonList("image.jpg"))
			.score(5)
			.createdAt(LocalDateTime.now())
			.build();

		GetReviewListResponse response = GetReviewListResponse.builder()
			.getReviewResponses(Collections.singletonList(reviewResponse))
			.build();

		when(reviewService.bookReviews(1L)).thenReturn(response);

		mockMvc.perform(get("/api/v1/store/reviews/books/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.getReviewResponses[0].nickName").value("John"))
			.andExpect(jsonPath("$.body.getReviewResponses[0].reviewId").value(1L))
			.andExpect(jsonPath("$.body.getReviewResponses[0].orderBookId").value(1L))
			.andExpect(jsonPath("$.body.getReviewResponses[0].content").value("Great book!"))
			.andExpect(jsonPath("$.body.getReviewResponses[0].reviewImages[0]").value("image.jpg"))
			.andExpect(jsonPath("$.body.getReviewResponses[0].score").value(5));
	}

	@Test
	void testCreateReviewed() throws Exception {

		CreateReviewResponse response = CreateReviewResponse.builder()
			.id(1L)
			.build();

		when(reviewService.createReview(anyLong(), any(CreateReviewRequest.class), any())).thenReturn(response);

		mockMvc.perform(post("/api/v1/store/reviews/1")
				.contentType("application/json")
				.content(
					"{ \"content\": \"Great book!\", \"score\": 5, \"reviewImageDTOs\": [{ \"fileName\": \"image1.jpg\", \"fileType\": \"image/jpeg\", \"data\": \"imageData1\" }] }")
				.with(csrf())
				.with(user("admin").roles("MEMBERS", "ADMIN")))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.body.id").value(1L));
	}

	@Test
	void testUpdateReviewed() throws Exception {

		doNothing().when(reviewService).updateReview(any(UpdateReviewRequest.class), anyLong());

		mockMvc.perform(put("/api/v1/store/reviews/reviews/1")
				.contentType("application/json")
				.content("{ \"bookId\": 1, \"content\": \"Updated content\", \"score\": 4 }")
				.with(csrf())
				.with(user("admin").roles("MEMBERS", "ADMIN")))
			.andExpect(status().isOk());
	}
}
