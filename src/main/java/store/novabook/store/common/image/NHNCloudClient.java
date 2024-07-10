package store.novabook.store.common.image;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "nhnCloudClient", url = "https://api-image.nhncloudservice.com")
public interface NHNCloudClient {
	@PutMapping(value = "/image/v2.0/appkeys/{appKey}/images", consumes = "application/octet-stream")
	ResponseEntity<String> uploadImage(
		@PathVariable("appKey") String appKey,
		@RequestParam("path") String path,
		@RequestParam("overwrite") boolean overwrite,
		@RequestHeader("Authorization") String authorization,
		@RequestParam("autorename") boolean autorename,
		@RequestBody FileSystemResource file
	);
}
