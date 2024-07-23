package store.novabook.store.image.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import store.novabook.store.book.dto.request.ReviewImageDTO;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.Review;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.InternalServerException;
import store.novabook.store.common.image.NHNCloudClient;
import store.novabook.store.common.image.NHNCloudMutilpartClient;
import store.novabook.store.common.util.FileConverter;
import store.novabook.store.common.util.KeyManagerUtil;
import store.novabook.store.common.util.dto.ImageManagerDto;
import store.novabook.store.image.entity.BookImage;
import store.novabook.store.image.entity.Image;
import store.novabook.store.image.entity.ReviewImage;
import store.novabook.store.image.repository.BookImageRepository;
import store.novabook.store.image.repository.ImageRepository;
import store.novabook.store.image.repository.ReviewImageRepository;
import store.novabook.store.image.service.ImageService;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ImageServiceImpl implements ImageService, InitializingBean {
	private final ImageRepository imageRepository;
	private final ReviewImageRepository reviewImageRepository;
	private final BookImageRepository bookImageRepository;
	private final NHNCloudMutilpartClient nhnCloudMutilpartClient;
	private final NHNCloudClient nhnCloudClient;
	private final Environment environment;

	private ImageManagerDto imageManagerDto;

	@Override
	public void afterPropertiesSet() {
		RestTemplate restTemplate = new RestTemplate();
		this.imageManagerDto = KeyManagerUtil.getImageManager(environment, restTemplate);
	}

	public void createBookImage(Book book, String requestImage) {
		if (imageManagerDto == null) {
			RestTemplate restTemplate = new RestTemplate();
			this.imageManagerDto = KeyManagerUtil.getImageManager(environment, restTemplate);
		}
		String fileName = requestImage.substring(requestImage.lastIndexOf("/") + 1);
		String outputFilePath = "/%s%s".formatted("src/main/resources/image", fileName);

		Path imagePath = Paths.get(outputFilePath);

		try (InputStream in = new URI(requestImage).toURL().openStream()) {
			Files.copy(in, imagePath);
		} catch (IOException | URISyntaxException e) {
			log.error("Failed to download file: {}. Error: {}", requestImage, e.getMessage(), e);
			fileDelete(imagePath, outputFilePath);
		}

		String nhnUrl = uploadBookImage(imageManagerDto.accessKey(), imageManagerDto.secretKey(),
			imageManagerDto.bucketName() + fileName, outputFilePath);

		Image image = imageRepository.save(new Image(nhnUrl));
		bookImageRepository.save(BookImage.of(book, image));

		fileDelete(imagePath, outputFilePath);

	}

	public void fileDelete(Path imagePath, String outputFilePath) {
		if (Files.exists(imagePath)) {
			try {
				Files.delete(imagePath);
				log.info("Successfully deleted file: {}", outputFilePath);
			} catch (IOException ex) {
				log.error("Failed to delete file: {}. Error: {}", outputFilePath, ex.getMessage(), ex);
			}
		}
	}

	//review 를 사용
	public void createReviewImage(Review review, List<ReviewImageDTO> reviewImageDTOs) {
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

	private String uploadBookImage(String appKey, String secretKey, String path,
		String localFilePath) {

		try {
			File file = new File(localFilePath);
			FileSystemResource resource = new FileSystemResource(file);

			ResponseEntity<String> response = nhnCloudClient.uploadImage(appKey, path, false, secretKey, true,
				resource);
			String jsonResponse = response.getBody();

			// JSON 응답을 파싱하여 URL 필드를 추출
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, new TypeReference<>() {
			});

			HashMap<String, Object> fileMap = (HashMap<String, Object>)responseMap.get("file");

			return (String)fileMap.get("url");

		} catch (Exception e) {
			log.error("Failed to nhnCloud : {}", e.getMessage());
			throw new InternalServerException(ErrorCode.FAILED_CREATE_BOOK);
		}
	}

}
