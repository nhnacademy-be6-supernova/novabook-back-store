package store.novabook.store.image.service;

import store.novabook.store.image.dto.request.CreateReviewImageRequest;
import store.novabook.store.orders.dto.CreateResponse;

public interface ReviewImageService {
	CreateResponse createReviewImage(CreateReviewImageRequest request);
}
