package store.novabook.store.book.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import store.novabook.store.book.dto.request.CreateReviewRequest;
import store.novabook.store.book.dto.response.CreateReviewResponse;
import store.novabook.store.book.entity.Review;
import store.novabook.store.book.repository.ReviewRepository;
import store.novabook.store.common.exception.BadRequestException;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.common.image.NHNCloudMutilpartClient;
import store.novabook.store.image.entity.Image;
import store.novabook.store.image.entity.ReviewImage;
import store.novabook.store.image.repository.ImageRepository;
import store.novabook.store.image.repository.ReviewImageRepository;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.repository.MemberRepository;
import store.novabook.store.orders.entity.OrdersBook;
import store.novabook.store.orders.repository.OrdersBookRepository;
import store.novabook.store.point.entity.PointHistory;
import store.novabook.store.point.entity.PointPolicy;
import store.novabook.store.point.repository.PointHistoryRepository;
import store.novabook.store.point.repository.PointPolicyRepository;

@ActiveProfiles("local")
@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

	@Mock
	private ReviewRepository reviewRepository;

	@Mock
	private OrdersBookRepository ordersBookRepository;
	@Mock
	private NHNCloudMutilpartClient nhnCloudClient;
	@Mock
	private ImageRepository imageRepository;
	@Mock
	private ReviewImageRepository reviewImageRepository;
	@Mock
	private PointHistoryRepository pointHistoryRepository;
	@Mock
	private PointPolicyRepository pointPolicyRepository;
	@Mock
	private MemberRepository memberRepository;

	@Mock
	private Environment environment;

	@InjectMocks
	private ReviewServiceImpl reviewService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateReview_Success() {
		// given
		Long ordersBookId = 1L;
		Long memberId = 1L;
		CreateReviewRequest request = mock(CreateReviewRequest.class);
		OrdersBook ordersBook = mock(OrdersBook.class);
		Member member = mock(Member.class);
		PointPolicy pointPolicy = mock(PointPolicy.class);
		Review review = mock(Review.class);

		when(reviewRepository.existsByOrdersBookId(ordersBookId)).thenReturn(false);
		when(ordersBookRepository.findById(ordersBookId)).thenReturn(Optional.of(ordersBook));
		when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
		when(pointPolicyRepository.findTopByOrderByCreatedAtDesc()).thenReturn(Optional.of(pointPolicy));
		when(reviewRepository.save(any(Review.class))).thenReturn(review);
		when(nhnCloudClient.uploadImagesAndGetRecord(anyString(), anyString(), anyList(), anyString())).thenReturn(
			ResponseEntity.ok("{ \"successes\": [ { \"url\": \"http://example.com/image.jpg\" } ] }"));

		// when
		CreateReviewResponse response = reviewService.createReview(ordersBookId, request, memberId);

		// then
		assertNotNull(response);
		verify(reviewRepository, times(1)).save(any(Review.class));
		verify(imageRepository, times(1)).save(any(Image.class));
		verify(reviewImageRepository, times(1)).save(any(ReviewImage.class));
		verify(pointHistoryRepository, times(1)).save(any(PointHistory.class));
	}

	@Test
	void testCreateReview_ReviewAlreadyExists() {
		// given
		Long ordersBookId = 1L;
		Long memberId = 1L;
		CreateReviewRequest request = mock(CreateReviewRequest.class);

		when(reviewRepository.existsByOrdersBookId(ordersBookId)).thenReturn(true);

		// when & then
		assertThrows(BadRequestException.class, () -> reviewService.createReview(ordersBookId, request, memberId));
	}

	@Test
	void testCreateReview_OrderBookNotFound() {
		// given
		Long ordersBookId = 1L;
		Long memberId = 1L;
		CreateReviewRequest request = mock(CreateReviewRequest.class);

		when(reviewRepository.existsByOrdersBookId(ordersBookId)).thenReturn(false);
		when(ordersBookRepository.findById(ordersBookId)).thenReturn(Optional.empty());

		// when & then
		assertThrows(NotFoundException.class, () -> reviewService.createReview(ordersBookId, request, memberId));
	}

	@Test
	void testCreateReview_MemberNotFound() {
		// given
		Long ordersBookId = 1L;
		Long memberId = 1L;
		CreateReviewRequest request = mock(CreateReviewRequest.class);
		OrdersBook ordersBook = mock(OrdersBook.class);

		when(reviewRepository.existsByOrdersBookId(anyLong())).thenReturn(false);
		when(ordersBookRepository.findById(anyLong())).thenReturn(Optional.of(ordersBook));
		when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

		// when & then
		assertThrows(NotFoundException.class, () -> reviewService.createReview(ordersBookId, request, memberId));
	}

	@Test
	void testCreateReview_PointPolicyNotFound() {
		// given
		Long ordersBookId = 1L;
		Long memberId = 1L;
		CreateReviewRequest request = mock(CreateReviewRequest.class);
		OrdersBook ordersBook = mock(OrdersBook.class);
		Member member = mock(Member.class);

		when(reviewRepository.existsByOrdersBookId(ordersBookId)).thenReturn(false);
		when(ordersBookRepository.findById(ordersBookId)).thenReturn(Optional.of(ordersBook));
		when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
		when(pointPolicyRepository.findTopByOrderByCreatedAtDesc()).thenReturn(Optional.empty());

		// when & then
		assertThrows(NotFoundException.class, () -> reviewService.createReview(ordersBookId, request, memberId));
	}
}
