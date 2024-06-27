package store.novabook.store.book.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto.CreateBookRequest;
import store.novabook.store.book.dto.CreateBookResponse;
import store.novabook.store.book.dto.GetBookAllResponse;
import store.novabook.store.book.dto.GetBookResponse;
import store.novabook.store.book.dto.UpdateBookRequest;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.BookStatus;
import store.novabook.store.book.repository.BookQueryRepository;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.book.repository.BookStatusRepository;
import store.novabook.store.book.repository.LikesRepository;
import store.novabook.store.category.entity.BookCategory;
import store.novabook.store.category.entity.Category;
import store.novabook.store.category.repository.BookCategoryRepository;
import store.novabook.store.category.repository.CategoryRepository;
import store.novabook.store.category.service.CategoryService;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.common.exception.FailedCreateBookException;
import store.novabook.store.common.image.NHNCloudClient;
import store.novabook.store.image.entity.BookImage;
import store.novabook.store.image.entity.Image;
import store.novabook.store.image.repository.BookImageRepository;
import store.novabook.store.image.repository.ImageRepository;
import store.novabook.store.tag.entity.BookTag;
import store.novabook.store.tag.entity.Tag;
import store.novabook.store.tag.repository.BookTagRepository;
import store.novabook.store.tag.repository.TagRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {
	private final BookRepository bookRepository;
	private final BookStatusRepository bookStatusRepository;
	private final LikesRepository likesRepository;
	private final BookTagRepository bookTagRepository;
	private final CategoryRepository categoryRepository;
	private final TagRepository tagRepository;
	private final BookCategoryRepository bookCategoryRepository;
	private final BookQueryRepository queryRepository;
	private final ImageRepository imageRepository;
	private final BookImageRepository bookImageRepository;
	private final CategoryService categoryService;
	private final NHNCloudClient nhnCloudClient;

	@Value("${nhn.cloud.imageManager.endpointUrl}")
	private String endpointUrl;

	@Value("${nhn.cloud.imageManager.accessKey}")
	private String accessKey;

	@Value("${nhn.cloud.imageManager.secretKey}")
	private String secretKey;

	@Value("${nhn.cloud.imageManager.bucketName}")
	private String bucketName;

	public CreateBookResponse create(CreateBookRequest request) {
		BookStatus bookStatus = bookStatusRepository.findById(request.bookStatusId())
			.orElseThrow(() -> new EntityNotFoundException(BookStatus.class, request.bookStatusId()));

		Book book = bookRepository.save(Book.of(request, bookStatus));

		List<Tag> tags = tagRepository.findByIdIn(request.tags());
		List<BookTag> bookTags = tags.stream()
			.map(tag -> new BookTag(book, tag))
			.toList();
		bookTagRepository.saveAll(bookTags);

		Category category = categoryRepository.findById(request.categoryId())
			.orElseThrow(() -> new EntityNotFoundException(Category.class, request.categoryId()));
		BookCategory bookCategories = BookCategory.of(book, category);
		bookCategoryRepository.save(bookCategories);

		String imageUrl = request.image();
		String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
		String outputFilePath = "/Users/mun/novabook/novabook-back-store/src/main/resources/image/"+fileName;
		try(InputStream in = new URI(imageUrl).toURL().openStream()){
			Path imagePath = Paths.get(outputFilePath);
			Files.copy(in, imagePath);
		} catch (IOException | URISyntaxException e) {
			throw new FailedCreateBookException();
		}
		uploadImage( accessKey,  secretKey, bucketName + fileName, false, outputFilePath);
		Image image = imageRepository.save(new Image(fileName));
		bookImageRepository.save(BookImage.of(book, image));

		return new CreateBookResponse(book.getId());
	}

	@Transactional(readOnly = true)
	public GetBookResponse getBook(Long id) {
		return queryRepository.getBook(id);
	}

	@Transactional(readOnly = true)
	public Page<GetBookAllResponse> getBookAll(Pageable pageable) {
		Page<Book> books = bookRepository.findAll(pageable);
		Page<GetBookAllResponse> booksResponse = books.map(GetBookAllResponse::fromEntity);

		return new PageImpl<>(booksResponse.getContent(), pageable, books.getTotalElements());
	}

	public void update(UpdateBookRequest request) {
		BookStatus bookStatus = bookStatusRepository.findById(request.bookStatusId())
			.orElseThrow(() -> new EntityNotFoundException(BookStatus.class, request.bookStatusId()));

		Book book = bookRepository.findById(request.id())
			.orElseThrow(() -> new EntityNotFoundException(Book.class, request.id()));

		book.update(bookStatus, request);
	}

	public void delete(Long id) {
		BookStatus bookStatus = bookStatusRepository.findById(4L)
			.orElseThrow(() -> new EntityNotFoundException(BookStatus.class, id));

		Book book = bookRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Book.class, id));
		book.updateBookStatus(bookStatus);
	}


	public void uploadImage(String appKey, String secretKey, String path, boolean overwrite, String localFilePath) {

		try {
			File file = new File(localFilePath);
			FileSystemResource resource = new FileSystemResource(file);
			nhnCloudClient.uploadImage(appKey, path, overwrite, secretKey, resource);
		} catch (Exception e) {
			throw new FailedCreateBookException();
		}
	}

}
