package store.novabook.store.image.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record FileUploadDTO(String fileName, MultipartFile file) {
	public static FileUploadDTO from(MultipartFile file) {
		return new FileUploadDTO(file.getOriginalFilename(), file);
	}
}
