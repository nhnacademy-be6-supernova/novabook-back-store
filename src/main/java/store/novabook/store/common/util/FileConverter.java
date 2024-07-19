package store.novabook.store.common.util;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import store.novabook.store.book.dto.request.ReviewImageDTO;

public class FileConverter {

	private FileConverter() {
		throw new IllegalStateException("유틸리티 클래스입니다.");
	}

	public static List<MultipartFile> convertToMultipartFile(List<ReviewImageDTO> imageDto) {
		List<MultipartFile> files = new ArrayList<>();
		for (ReviewImageDTO dto : imageDto) {
			byte[] data = Base64.getDecoder().decode(dto.data());
			files.add(new CustomMultipartFile(dto.fileName(), dto.fileType(), data));
		}
		return files;
	}
}