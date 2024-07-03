package store.novabook.store.common.util;

import java.util.Base64;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import store.novabook.store.book.dto.request.ReviewImageDTO;

public class FileConverter {

	public static MultipartFile convertToMultipartFile(ReviewImageDTO imageDTO) throws IOException {
		byte[] data = Base64.getDecoder().decode(imageDTO.data());
		return new CustomMultipartFile(
			imageDTO.fileName(),
			imageDTO.fileType(),
			data
		);
	}
}