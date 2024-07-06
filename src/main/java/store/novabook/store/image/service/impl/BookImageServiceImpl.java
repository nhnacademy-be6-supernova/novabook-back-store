package store.novabook.store.image.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.image.dto.request.CreateBookImageRequest;
import store.novabook.store.image.entity.BookImage;
import store.novabook.store.image.entity.Image;
import store.novabook.store.image.repository.BookImageRepository;
import store.novabook.store.image.repository.ImageRepository;
import store.novabook.store.image.service.BookImageService;
import store.novabook.store.orders.dto.response.CreateResponse;

@Service
@RequiredArgsConstructor
@Transactional
public class BookImageServiceImpl implements BookImageService {
	private final ImageRepository imageRepository;
	private final BookRepository bookRepository;
	private final BookImageRepository bookImageRepository;

	@Override
	public CreateResponse createBookImage(CreateBookImageRequest request) {
		Book book = bookRepository.findById(request.bookId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_NOT_FOUND));
		Image image = imageRepository.findById(request.imageId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.IMAGE_NOT_FOUND));
		BookImage bookImage = new BookImage(book, image);
		bookImageRepository.save(bookImage);
		return new CreateResponse(bookImage.getId());
	}
}
