package store.novabook.store.image.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.image.dto.request.CreateImageRequest;
import store.novabook.store.image.entity.Image;
import store.novabook.store.image.repository.ImageRepository;
import store.novabook.store.orders.dto.CreateResponse;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {
	private final ImageRepository imageRepository;

	public CreateResponse createImage(CreateImageRequest request) {
		Image image = new Image(request);
		imageRepository.save(image);
		return new CreateResponse(image.getId());
	}


}
