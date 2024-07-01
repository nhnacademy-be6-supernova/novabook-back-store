package store.novabook.store.image.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.entity.Review;
import store.novabook.store.book.repository.ReviewRepository;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.image.dto.request.CreateReviewImageRequest;
import store.novabook.store.image.entity.Image;
import store.novabook.store.image.entity.ReviewImage;
import store.novabook.store.image.repository.ImageRepository;
import store.novabook.store.image.repository.ReviewImageRepository;
import store.novabook.store.image.service.ReviewImageService;
import store.novabook.store.orders.dto.CreateResponse;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewImageServiceImpl implements ReviewImageService {
	private final ReviewRepository reviewRepository;
	private final ImageRepository imageRepository;
	private final ReviewImageRepository reviewImageRepository;

	@Override
	public CreateResponse createReviewImage(CreateReviewImageRequest request) {
		Review review = reviewRepository.findById(request.reviewId())
			.orElseThrow(()-> new EntityNotFoundException(Review.class, request.reviewId()));
		Image image = imageRepository.findById(request.imageId())
			.orElseThrow(()-> new EntityNotFoundException(Image.class, request.imageId()));
		ReviewImage reviewImage = new ReviewImage(review, image);

		reviewImageRepository.save(reviewImage);

		return new CreateResponse(reviewImage.getId());
	}


}
