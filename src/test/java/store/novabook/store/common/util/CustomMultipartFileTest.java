package store.novabook.store.common.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

class CustomMultipartFileTest {

	@Test
	void testGetName() {
		MultipartFile multipartFile = new CustomMultipartFile("test.txt", "text/plain", "Hello".getBytes());
		assertEquals("test.txt", multipartFile.getName());
	}

	@Test
	void testGetOriginalFilename() {
		MultipartFile multipartFile = new CustomMultipartFile("test.txt", "text/plain", "Hello".getBytes());
		assertEquals("test.txt", multipartFile.getOriginalFilename());
	}

	@Test
	void testGetContentType() {
		MultipartFile multipartFile = new CustomMultipartFile("test.txt", "text/plain", "Hello".getBytes());
		assertEquals("text/plain", multipartFile.getContentType());
	}

	@Test
	void testIsEmpty() {
		MultipartFile multipartFile = new CustomMultipartFile("test.txt", "text/plain", "Hello".getBytes());
		assertFalse(multipartFile.isEmpty());

		MultipartFile emptyMultipartFile = new CustomMultipartFile("test.txt", "text/plain", new byte[0]);
		assertTrue(emptyMultipartFile.isEmpty());
	}

	@Test
	void testGetSize() {
		MultipartFile multipartFile = new CustomMultipartFile("test.txt", "text/plain", "Hello".getBytes());
		assertEquals(5, multipartFile.getSize());
	}

	@Test
	void testGetBytes() throws IOException {
		MultipartFile multipartFile = new CustomMultipartFile("test.txt", "text/plain", "Hello".getBytes());
		assertArrayEquals("Hello".getBytes(), multipartFile.getBytes());
	}

	@Test
	void testGetInputStream() throws IOException {
		MultipartFile multipartFile = new CustomMultipartFile("test.txt", "text/plain", "Hello".getBytes());
		ByteArrayInputStream expectedInputStream = new ByteArrayInputStream("Hello".getBytes());
		InputStream actualInputStream = multipartFile.getInputStream();

		int expectedByte;
		while ((expectedByte = expectedInputStream.read()) != -1) {
			assertEquals(expectedByte, actualInputStream.read());
		}
		assertEquals(-1, actualInputStream.read()); // End of stream
	}

	@Test
	void testTransferTo() throws IOException {
		MultipartFile multipartFile = new CustomMultipartFile("test.txt", "text/plain", "Hello".getBytes());
		Path tempFile = Files.createTempFile("test", ".txt");
		File destinationFile = tempFile.toFile();

		multipartFile.transferTo(destinationFile);

		byte[] fileContent = Files.readAllBytes(tempFile);
		assertArrayEquals("Hello".getBytes(), fileContent);

		// Clean up
		Files.delete(tempFile);
	}
}
