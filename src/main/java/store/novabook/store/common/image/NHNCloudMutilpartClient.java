package store.novabook.store.common.image;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import feign.Headers;

@FeignClient(name = "nhnCloudMutilpartClient", url = "https://api-image.nhncloudservice.com")
public interface NHNCloudMutilpartClient {

	/**
	 * 이미지 업로드 후 레코드 형태의 응답을 받는 메서드입니다.
	 *
	 * @param appKey      애플리케이션 키
	 * @param secretKey   시크릿 키 (Authorization 헤더로 전달)
	 * @param files       업로드할 이미지 파일 리스트
	 * @return 업로드된 이미지 정보 레코드 리스트
	 */
	@PostMapping(value = "/image/v2.0/appkeys/{appKey}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Headers("Content-Type: multipart/form-data")
	ResponseEntity<String> uploadImagesAndGetRecord(
		@PathVariable("appKey") String appKey,
		@RequestPart("params") String params,
		@RequestPart("files") List<MultipartFile> files,
		@RequestHeader("Authorization") String secretKey
	);
}

