package store.novabook.store.common.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public class CustomMultipartFile implements MultipartFile {

	private final String fileName;
	private final String contentType;
	private final byte[] content;

	public CustomMultipartFile(String fileName, String contentType, byte[] content) {
		this.fileName = fileName;
		this.contentType = contentType;
		this.content = content;
	}

	@Override
	public String getName() {
		return this.fileName;
	}

	@Override
	public String getOriginalFilename() {
		return this.fileName;
	}

	@Override
	public String getContentType() {
		return this.contentType;
	}

	@Override
	public boolean isEmpty() {
		return this.content.length == 0;
	}

	@Override
	public long getSize() {
		return this.content.length;
	}

	@Override
	public byte[] getBytes() throws IOException {
		return this.content;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(this.content);
	}

	@Override
	public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
		java.nio.file.Files.write(dest.toPath(), this.content);
	}
}
