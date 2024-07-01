package store.novabook.store.image.service;

import store.novabook.store.image.dto.request.CreateBookImageRequest;
import store.novabook.store.orders.dto.CreateResponse;

public interface BookImageService {
	CreateResponse createBookImage(CreateBookImageRequest request);
}
