package store.novabook.store.book.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.book.dto.CreateBookRequest;
import store.novabook.store.book.dto.CreateBookResponse;
import store.novabook.store.book.dto.GetBookAllResponse;
import store.novabook.store.book.dto.GetBookResponse;
import store.novabook.store.book.dto.UpdateBookRequest;

public interface BookService {
	CreateBookResponse create(CreateBookRequest request);

	@Transactional(readOnly = true)
	GetBookResponse getBook(Long id);

	@Transactional(readOnly = true)
	Page<GetBookAllResponse> getBookAll(Pageable pageable);

	void update(UpdateBookRequest request);

	void delete(Long id);

	String uploadImage(String appKey, String secretKey, String path, boolean overwrite, String localFilePath);
}
