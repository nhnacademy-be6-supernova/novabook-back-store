package store.novabook.store.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import store.novabook.store.book.dto.request.ReviewImageDTO;

public class FileConverter {
	public static List<MultipartFile> convertToMultipartFile(List<ReviewImageDTO> imageDTO) throws IOException {
		List<MultipartFile> files = new ArrayList<>();
		for (ReviewImageDTO dto : imageDTO) {
			byte[] data = Base64.getDecoder().decode(dto.data());
			files.add(new CustomMultipartFile(
				dto.fileName(),
				dto.fileType(),
				data
			));
		}
		return files;
	}
}