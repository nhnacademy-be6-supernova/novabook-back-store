package store.novabook.store.common.image;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import feign.RequestLine;

@FeignClient(name = "nhnCloudClient", url = "https://api-image.nhncloudservice.com")
public interface NHNCloudClient {
	@PostMapping(value = "/image/v2.0/appkeys/{appKey}/images", consumes = "application/octet-stream")
	ResponseEntity<String> uploadImage(
		@PathVariable("appKey") String appKey,
		@RequestParam("path") String path,
		@RequestParam("overwrite") boolean overwrite,
		@RequestHeader("Authorization") String authorization,
		@RequestParam("autorename") boolean autorename,
		@RequestBody FileSystemResource file
	);
}
