package store.novabook.store.image.service;

import java.util.List;

import store.novabook.store.book.dto.request.ReviewImageDTO;
import store.novabook.store.book.entity.Review;

public interface ImageService {
	void createReviewImageDtos(Review review, List<ReviewImageDTO> reviewImageDTOs);
}
