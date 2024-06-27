package store.novabook.store.image.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto.CreateBookRequest;
import store.novabook.store.book.dto.CreateReviewRequest;
import store.novabook.store.common.image.NHNCloudClient;
import store.novabook.store.image.dto.request.CreateImageResponse;
import store.novabook.store.image.entity.Image;
import store.novabook.store.image.repository.ImageRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {
	private final ImageRepository imageRepository;
	private final NHNCloudClient nhnCloudClient;

	@Value("${nhn.cloud.imageManager.endpointUrl}")
	private String endpointUrl;

	@Value("${nhn.cloud.imageManager.accessKey}")
	private String accessKey;

	@Value("${nhn.cloud.imageManager.secretKey}")
	private String secretKey;

	@Value("${nhn.cloud.imageManager.bucketName}")
	private String bucketName;

	public CreateImageResponse createImage(CreateBookRequest createBookRequest) {
		Image image = imageRepository.save(new Image(createBookRequest));
		return CreateImageResponse.formEntity(image);
	}

	public CreateImageResponse createImage(CreateReviewRequest createBookRequest) {
		Image image = imageRepository.save(new Image(createBookRequest));
		return CreateImageResponse.formEntity(image);
	}
}
