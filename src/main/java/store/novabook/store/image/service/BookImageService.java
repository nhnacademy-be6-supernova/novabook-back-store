package store.novabook.store.image.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.image.dto.request.CreateBookImageRequest;
import store.novabook.store.image.entity.BookImage;
import store.novabook.store.image.entity.Image;
import store.novabook.store.image.repository.BookImageRepository;
import store.novabook.store.image.repository.ImageRepository;
import store.novabook.store.orders.dto.CreateResponse;

@Service
@RequiredArgsConstructor
@Transactional
public class BookImageService {
	private final ImageRepository imageRepository;
	private final BookRepository bookRepository;
	private final BookImageRepository bookImageRepository;

	public CreateResponse createBookImage(CreateBookImageRequest request){
		Book book = bookRepository.findById(request.bookId())
			.orElseThrow(()-> new EntityNotFoundException(Book.class, request.bookId()));
		Image image = imageRepository.findById(request.imageId())
			.orElseThrow(()-> new EntityNotFoundException(Image.class, request.imageId()));
		BookImage bookImage = new BookImage(book, image);

		 bookImageRepository.save(bookImage);

		return new CreateResponse(bookImage.getId());
	}
}
