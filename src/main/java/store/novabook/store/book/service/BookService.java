package store.novabook.store.book.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.book.dto.request.CreateBookRequest;
import store.novabook.store.book.dto.request.UpdateBookRequest;
import store.novabook.store.book.dto.response.CreateBookResponse;
import store.novabook.store.book.dto.response.GetBookAllResponse;
import store.novabook.store.book.dto.response.GetBookResponse;
import store.novabook.store.book.dto.response.GetBookToMainResponseMap;

public interface BookService {
	CreateBookResponse create(CreateBookRequest request);

	@Transactional(readOnly = true)
	GetBookResponse getBook(Long id);

	@Transactional(readOnly = true)
	Page<GetBookAllResponse> getBookAll(Pageable pageable);

	void update(UpdateBookRequest request);

	void delete(Long id);

	String uploadImage(String appKey, String secretKey, String path, boolean overwrite, String localFilePath);

	GetBookToMainResponseMap getBookToMainPage();
}
