package store.novabook.store.image.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto.request.ReviewImageDTO;
import store.novabook.store.book.entity.Review;
import store.novabook.store.common.image.NHNCloudMutilpartClient;
import store.novabook.store.common.util.FileConverter;
import store.novabook.store.common.util.KeyManagerUtil;
import store.novabook.store.common.util.dto.ImageManagerDto;
import store.novabook.store.image.entity.Image;
import store.novabook.store.image.entity.ReviewImage;
import store.novabook.store.image.repository.BookImageRepository;
import store.novabook.store.image.repository.ImageRepository;
import store.novabook.store.image.repository.ReviewImageRepository;
import store.novabook.store.image.service.ImageService;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageServiceImpl implements ImageService {
	private final ImageRepository imageRepository;
	private final ReviewImageRepository reviewImageRepository;
	private final BookImageRepository bookImageRepository;
	private final NHNCloudMutilpartClient nhnCloudMutilpartClient;

	private final Environment environment;
	private ImageManagerDto imageManagerDto;


	//review 를 사용
	public void createReviewImageDtos(Review review, List<ReviewImageDTO> reviewImageDTOs) {
		if (imageManagerDto == null) {
			RestTemplate restTemplate = new RestTemplate();
			this.imageManagerDto = KeyManagerUtil.getImageManager(environment, restTemplate);
		}
		//리뷰 이미지를 저장
		String basepath = imageManagerDto.bucketName() + "review";
		//NHN클라우드 설정
		String paramsJson = "{\"basepath\": \"" + basepath + "\", \"overwrite\": true, \"autorename\": \"true\"}";

		List<String> imageList = uploadImage(imageManagerDto.accessKey(), paramsJson, imageManagerDto.secretKey(),
			reviewImageDTOs);

		imageList.forEach(imageUrl -> {
			Image image1 = imageRepository.save(new Image(imageUrl));
			reviewImageRepository.save(new ReviewImage(review, image1));
		});

	}

	private List<String> uploadImage(String appKey, String params, String secretKey,
		List<ReviewImageDTO> reviewImageDTO) {
		try {
			List<MultipartFile> resource = FileConverter.convertToMultipartFile(reviewImageDTO);
			String jsonResponse = nhnCloudMutilpartClient.uploadImagesAndGetRecord(appKey, params, resource, secretKey)
				.getBody();
			// JSON 응답을 파싱하여 URL 필드를 추출
			return extractUrls(jsonResponse);
		} catch (IOException e) {
			return Collections.emptyList();
		}
	}

	private List<String> extractUrls(String jsonString) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(jsonString);

		List<String> urls = new ArrayList<>();

		// 성공 항목(successes)에서 URL 추출
		JsonNode successes = rootNode.path("successes");
		if (successes.isArray()) {
			for (JsonNode item : successes) {
				if (item.has("url")) {
					urls.add(item.get("url").asText());
				}
			}
		}

		return urls;
	}

}
