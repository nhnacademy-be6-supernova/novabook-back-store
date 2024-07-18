package store.novabook.store.book.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import store.novabook.store.book.dto.request.CreateBookRequest;
import store.novabook.store.book.dto.request.UpdateBookRequest;
import store.novabook.store.book.dto.response.CreateBookResponse;
import store.novabook.store.book.dto.response.GetBookAllResponse;
import store.novabook.store.book.dto.response.GetBookResponse;
import store.novabook.store.book.dto.response.GetBookToMainResponse;
import store.novabook.store.book.dto.response.GetBookToMainResponseMap;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.BookStatus;
import store.novabook.store.book.repository.BookQueryRepository;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.book.repository.BookStatusRepository;
import store.novabook.store.book.service.BookService;
import store.novabook.store.category.entity.BookCategory;
import store.novabook.store.category.entity.Category;
import store.novabook.store.category.repository.BookCategoryRepository;
import store.novabook.store.category.repository.CategoryRepository;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.InternalServerException;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.common.image.NHNCloudClient;
import store.novabook.store.common.util.KeyManagerUtil;
import store.novabook.store.common.util.dto.ImageManagerDto;
import store.novabook.store.image.entity.BookImage;
import store.novabook.store.image.entity.Image;
import store.novabook.store.image.repository.BookImageRepository;
import store.novabook.store.image.repository.ImageRepository;
import store.novabook.store.search.document.BookDocument;
import store.novabook.store.search.repository.BookSearchRepository;
import store.novabook.store.tag.entity.BookTag;
import store.novabook.store.tag.entity.Tag;
import store.novabook.store.tag.repository.BookTagRepository;
import store.novabook.store.tag.repository.TagRepository;

@Service
@Transactional
@Slf4j
public class BookServiceImpl implements BookService {
	private final BookRepository bookRepository;
	private final BookStatusRepository bookStatusRepository;
	private final BookTagRepository bookTagRepository;
	private final CategoryRepository categoryRepository;
	private final TagRepository tagRepository;
	private final BookCategoryRepository bookCategoryRepository;
	private final BookQueryRepository queryRepository;
	private final ImageRepository imageRepository;
	private final BookImageRepository bookImageRepository;
	private final NHNCloudClient nhnCloudClient;
	private final BookSearchRepository bookSearchRepository;
	private final ImageManagerDto imageManagerDto;

	public BookServiceImpl(BookRepository bookRepository, BookStatusRepository bookStatusRepository,
		BookTagRepository bookTagRepository, CategoryRepository categoryRepository, TagRepository tagRepository,
		BookCategoryRepository bookCategoryRepository, BookQueryRepository queryRepository,
		ImageRepository imageRepository, BookImageRepository bookImageRepository, NHNCloudClient nhnCloudClient,
		BookSearchRepository bookSearchRepository, Environment environment) {

		this.bookRepository = bookRepository;
		this.bookStatusRepository = bookStatusRepository;
		this.bookTagRepository = bookTagRepository;
		this.categoryRepository = categoryRepository;
		this.tagRepository = tagRepository;
		this.bookCategoryRepository = bookCategoryRepository;
		this.queryRepository = queryRepository;
		this.imageRepository = imageRepository;
		this.bookImageRepository = bookImageRepository;
		this.nhnCloudClient = nhnCloudClient;
		this.bookSearchRepository = bookSearchRepository;
		this.imageManagerDto = KeyManagerUtil.getImageManager(environment);
	}

	public CreateBookResponse create(CreateBookRequest request) {
		BookStatus bookStatus = bookStatusRepository.findById(request.bookStatusId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_STATUS_NOT_FOUND));

		Book book = bookRepository.save(Book.of(request, bookStatus));

		List<Tag> tags = tagRepository.findByIdIn(request.tags());
		List<BookTag> bookTags = tags.stream().map(tag -> new BookTag(book, tag)).toList();
		bookTagRepository.saveAll(bookTags);

		List<Category> categories = categoryRepository.findByIdIn(request.categories());
		List<BookCategory> bookCategories = categories.stream()
			.map(category -> new BookCategory(book, category))
			.toList();
		bookCategoryRepository.saveAll(bookCategories);

		String imageUrl = request.image();
		String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
		String outputFilePath = "/" + imageManagerDto.localStorage() + fileName;
		// String outputFilePath = "src/main/resources/image/" + fileName;

		Path imagePath = Paths.get(outputFilePath);

		try (InputStream in = new URI(imageUrl).toURL().openStream()) {
			Files.copy(in, imagePath);
		} catch (IOException | URISyntaxException e) {
			log.error("Failed to download file: {}. Error: {}", imageUrl, e.getMessage(), e);

			// 파일이 존재하는 경우에만 삭제 시도
			if (Files.exists(imagePath)) {
				try {
					Files.delete(imagePath);
					log.info("Successfully deleted file: {}", outputFilePath);
				} catch (IOException ex) {
					log.error("Failed to delete file: {}. Error: {}", outputFilePath, ex.getMessage(), ex);
				}
			}
		}

		String nhnUrl = uploadImage(imageManagerDto.accessKey(), imageManagerDto.secretKey(),
			imageManagerDto.bucketName() + fileName, false, outputFilePath);

		Image image = imageRepository.save(new Image(nhnUrl));
		bookImageRepository.save(BookImage.of(book, image));

		bookSearchRepository.save(BookDocument.of(book, image, tags, categories, 0.0, 0));

		return new CreateBookResponse(book.getId());
	}

	@Transactional(readOnly = true)
	public GetBookResponse getBook(Long id) {
		return queryRepository.getBook(id);
	}

	@Transactional(readOnly = true)
	public Page<GetBookAllResponse> getBookAll(Pageable pageable) {
		Page<Book> books = bookRepository.findAll(pageable);
		return books.map(GetBookAllResponse::fromEntity);
	}

	public void update(UpdateBookRequest request) {
		BookStatus bookStatus = bookStatusRepository.findById(request.bookStatusId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_STATUS_NOT_FOUND));

		Book book = bookRepository.findById(request.id())
			.orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_NOT_FOUND));

		book.update(bookStatus, request);
	}

	public void delete(Long id) {
		BookStatus bookStatus = bookStatusRepository.findById(4L)
			.orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_STATUS_NOT_FOUND));

		Book book = bookRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_NOT_FOUND));
		book.updateBookStatus(bookStatus);
	}

	public String uploadImage(String appKey, String secretKey, String path, boolean overwrite, String localFilePath) {

		try {
			File file = new File(localFilePath);
			FileSystemResource resource = new FileSystemResource(file);

			ResponseEntity<String> response = nhnCloudClient.uploadImage(appKey, path, overwrite, secretKey, true,
				resource);
			String jsonResponse = response.getBody();

			// JSON 응답을 파싱하여 URL 필드를 추출
			ObjectMapper objectMapper = new ObjectMapper();

			Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, Map.class);

			HashMap<String, Object> map = (HashMap<String, Object>)responseMap.get("file");

			return (String)map.get("url");

		} catch (Exception e) {
			log.error("Failed to nhnCloud : {}", e.getMessage());
			throw new InternalServerException(ErrorCode.FAILED_CREATE_BOOK);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public GetBookToMainResponseMap getBookToMainPage() {
		return queryRepository.getBookToMainPage();
	}
}
