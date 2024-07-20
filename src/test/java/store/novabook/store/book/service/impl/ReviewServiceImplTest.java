package store.novabook.store.book.service.impl;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import store.novabook.store.book.dto.ReviewImageDto;
import store.novabook.store.book.dto.request.CreateReviewRequest;
import store.novabook.store.book.dto.request.ReviewImageDTO;
import store.novabook.store.book.dto.request.UpdateReviewRequest;
import store.novabook.store.book.dto.response.CreateReviewResponse;
import store.novabook.store.book.dto.response.GetReviewListResponse;
import store.novabook.store.book.dto.response.GetReviewResponse;
import store.novabook.store.book.entity.Review;
import store.novabook.store.book.repository.ReviewRepository;
import store.novabook.store.common.exception.BadRequestException;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.image.service.ImageService;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.repository.MemberRepository;
import store.novabook.store.orders.entity.OrdersBook;
import store.novabook.store.orders.repository.OrdersBookRepository;
import store.novabook.store.point.entity.PointPolicy;
import store.novabook.store.point.repository.PointHistoryRepository;
import store.novabook.store.point.repository.PointPolicyRepository;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

	@Mock
	private ReviewRepository reviewRepository;

	@Mock
	private OrdersBookRepository ordersBookRepository;

	@Mock
	private PointPolicyRepository pointPolicyRepository;
	@Mock
	private PointHistoryRepository pointHistoryRepository;

	@Mock
	private MemberRepository memberRepository;
	@Mock
	private ImageService imageService;

	@InjectMocks
	private ReviewServiceImpl reviewService;

	private Review review;
	private OrdersBook ordersBook;
	private Member member;
	private PointPolicy pointPolicy;
	private CreateReviewRequest createReviewRequest;

	@BeforeEach
	void setUp() {
		ordersBook = mock(OrdersBook.class);
		member = mock(Member.class);
		pointPolicy = mock(PointPolicy.class);
		review = mock(Review.class);

		createReviewRequest = CreateReviewRequest.builder()
			.content("Great book!")
			.score(5)
			.reviewImageDTOs(Collections.singletonList(
				ReviewImageDTO.builder()
					.fileName("image.jpg")
					.fileType("image/jpeg")
					.data("imageData")
					.build()
			))
			.build();
	}

	@Test
	void testBookReviews() {
		List<ReviewImageDto> reviewImageDtoList = Arrays.asList(
			new ReviewImageDto("nick1", 1L, 1L, "Great book!", "image1.jpg", 5, null),
			new ReviewImageDto("nick2", 2L, 1L, "Not bad", "image2.jpg", 4, null)
		);

		when(reviewRepository.findReviewByBookId(1L)).thenReturn(reviewImageDtoList);

		GetReviewListResponse response = reviewService.bookReviews(1L);

		assertThat(response).isNotNull();
		assertThat(response.getReviewResponses()).hasSize(2);
	}

	@Test
	void testGetReviewById() {
		List<ReviewImageDto> reviewImageDtoList = List.of(
			new ReviewImageDto("nick1", 1L, 1L, "Great book!", "image1.jpg", 5, null)
		);

		when(reviewRepository.findReviewByReviewId(1L)).thenReturn(reviewImageDtoList);

		GetReviewResponse response = reviewService.getReviewById(1L);

		assertThat(response).isNotNull();
		assertThat(response.reviewId()).isEqualTo(1L);
	}

	@Test
	void testCreateReview_WithImages() {
		// 리뷰 이미지가 있는 경우의 요청 생성
		CreateReviewRequest requestWithImages = CreateReviewRequest.builder()
			.content("Great book!")
			.score(5)
			.reviewImageDTOs(Arrays.asList(
				ReviewImageDTO.builder()
					.fileName("image1.jpg")
					.fileType("image/jpeg")
					.data("imageData1")
					.build(),
				ReviewImageDTO.builder()
					.fileName("image2.jpg")
					.fileType("image/jpeg")
					.data("imageData2")
					.build()
			))
			.build();

		when(reviewRepository.existsByOrdersBookId(1L)).thenReturn(false);
		when(ordersBookRepository.findById(1L)).thenReturn(Optional.of(ordersBook));
		when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
		when(pointPolicyRepository.findTopByOrderByCreatedAtDesc()).thenReturn(Optional.of(pointPolicy));
		when(reviewRepository.save(any(Review.class))).thenReturn(review);

		CreateReviewResponse response = reviewService.createReview(1L, requestWithImages, 1L);

		assertThat(response).isNotNull();
		verify(imageService).createReviewImage(any(Review.class), eq(requestWithImages.reviewImageDTOs()));
	}

	@Test
	void testCreateReview_WithoutImages() {
		// 리뷰 이미지가 없는 경우의 요청 생성
		CreateReviewRequest requestWithoutImages = CreateReviewRequest.builder()
			.content("Great book!")
			.score(5)
			.reviewImageDTOs(Collections.singletonList(
				ReviewImageDTO.builder()
					.fileName("")
					.fileType("image/jpeg")
					.data("")
					.build()
			))
			.build();

		when(reviewRepository.existsByOrdersBookId(1L)).thenReturn(false);
		when(ordersBookRepository.findById(1L)).thenReturn(Optional.of(ordersBook));
		when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
		when(pointPolicyRepository.findTopByOrderByCreatedAtDesc()).thenReturn(Optional.of(pointPolicy));
		when(reviewRepository.save(any(Review.class))).thenReturn(review);

		CreateReviewResponse response = reviewService.createReview(1L, requestWithoutImages, 1L);

		assertThat(response).isNotNull();
		verify(imageService, never()).createReviewImage(any(Review.class), anyList());
	}

	@Test
	void testCreateReview_ThrowsBadRequestException() {
		when(reviewRepository.existsByOrdersBookId(1L)).thenReturn(true);

		assertThrows(BadRequestException.class, () -> {
			reviewService.createReview(1L, createReviewRequest, 1L);
		});
	}

	@Test
	void testUpdateReview() {
		UpdateReviewRequest updateReviewRequest = UpdateReviewRequest.builder()
			.bookId(1L)
			.content("Updated content")
			.score(4)
			.build();

		when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

		reviewService.updateReview(updateReviewRequest, 1L);

		verify(review).update(updateReviewRequest);
	}

	@Test
	void testUpdateReview_ThrowsNotFoundException() {
		UpdateReviewRequest updateReviewRequest = UpdateReviewRequest.builder()
			.bookId(1L)
			.content("Updated content")
			.score(4)
			.build();

		when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			reviewService.updateReview(updateReviewRequest, 1L);
		});
	}
}
