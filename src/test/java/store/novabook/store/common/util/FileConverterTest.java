package store.novabook.store.common.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import store.novabook.store.book.dto.request.ReviewImageDTO;

class FileConverterTest {

	@Test
	void testConvertToMultipartFile() throws IOException {
		// Given
		List<ReviewImageDTO> reviewImageDTOList = new ArrayList<>();
		reviewImageDTOList.add(ReviewImageDTO.builder()
			.fileName("test1.txt")
			.fileType("text/plain")
			.data(Base64.getEncoder().encodeToString("Hello1".getBytes()))
			.build());

		reviewImageDTOList.add(ReviewImageDTO.builder()
			.fileName("test2.txt")
			.fileType("text/plain")
			.data(Base64.getEncoder().encodeToString("Hello2".getBytes()))
			.build());

		// When
		List<MultipartFile> multipartFileList = FileConverter.convertToMultipartFile(reviewImageDTOList);

		// Then
		assertEquals(2, multipartFileList.size());

		MultipartFile file1 = multipartFileList.get(0);
		assertEquals("test1.txt", file1.getOriginalFilename());
		assertEquals("text/plain", file1.getContentType());
		assertArrayEquals("Hello1".getBytes(), file1.getBytes());

		MultipartFile file2 = multipartFileList.get(1);
		assertEquals("test2.txt", file2.getOriginalFilename());
		assertEquals("text/plain", file2.getContentType());
		assertArrayEquals("Hello2".getBytes(), file2.getBytes());
	}

	@Test
	void testConvertToMultipartFileWithEmptyList() throws IOException {
		// Given
		List<ReviewImageDTO> reviewImageDTOList = new ArrayList<>();

		// When
		List<MultipartFile> multipartFileList = FileConverter.convertToMultipartFile(reviewImageDTOList);

		// Then
		assertTrue(multipartFileList.isEmpty());
	}

	@Test
	void testConvertToMultipartFileWithInvalidBase64() {
		// Given
		List<ReviewImageDTO> reviewImageDTOList = new ArrayList<>();
		reviewImageDTOList.add(ReviewImageDTO.builder()
			.fileName("test.txt")
			.fileType("text/plain")
			.data("InvalidBase64Data")
			.build());

		// When / Then
		assertThrows(IllegalArgumentException.class, () -> FileConverter.convertToMultipartFile(reviewImageDTOList));
	}
}
