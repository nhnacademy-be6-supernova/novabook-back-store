package store.novabook.store.common.image;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;

import feign.Headers;
import feign.RequestLine;
import store.novabook.store.common.response.ImageUploadResponse;

import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(name = "nhnCloudMutilpartClient", url = "https://api-image.nhncloudservice.com")
public interface NHNCloudMutilpartClient {

	/**
	 * 이미지 업로드 후 레코드 형태의 응답을 받는 메서드입니다.
	 *
	 * @param appKey      애플리케이션 키
	 * @param secretKey   시크릿 키 (Authorization 헤더로 전달)
	 * @param basepath    업로드할 절대 경로
	 * @param overwrite   같은 이름이 있을 경우 덮어쓰기 여부
	 * @param operationIds 이미지 오퍼레이션 ID 리스트
	 * @param paramsJson  JSON 형식의 업로드 옵션 파라미터
	 * @param files       업로드할 이미지 파일 리스트
	 * @return 업로드된 이미지 정보 레코드 리스트
	 */
	@RequestLine("POST /image/v2.0/appkeys/{appKey}/images")
	@Headers("Authorization: {secretKey}")
	ImageUploadResponse uploadImagesAndGetRecord(
		@PathVariable("appKey") String appKey,
		@RequestPart("params") String paramsJson,
		@RequestPart("params.basepath") String basepath,
		@RequestPart("params.overwrite") boolean overwrite,
		@RequestPart("params.operationIds") String operationIds,
		@RequestPart("files") List<MultipartFile> files,
		@PathVariable("secretKey") String secretKey
	);
}

