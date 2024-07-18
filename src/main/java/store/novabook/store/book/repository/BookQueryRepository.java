package store.novabook.store.book.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import store.novabook.store.book.dto.response.GetBookResponse;
import store.novabook.store.book.dto.response.GetBookToMainResponse;
import store.novabook.store.book.dto.response.GetBookToMainResponseMap;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.QBook;
import store.novabook.store.book.entity.QBookStatus;
import store.novabook.store.book.entity.QLikes;
import store.novabook.store.book.entity.QReview;
import store.novabook.store.category.entity.QBookCategory;
import store.novabook.store.category.entity.QCategory;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.image.entity.Image;
import store.novabook.store.image.entity.QBookImage;
import store.novabook.store.image.entity.QImage;
import store.novabook.store.orders.entity.QOrdersBook;
import store.novabook.store.search.document.BookDocument;
import store.novabook.store.search.dto.GetBookSearchResponse;
import store.novabook.store.tag.entity.QBookTag;
import store.novabook.store.tag.entity.QTag;

@Repository
@Transactional(readOnly = true)
public class BookQueryRepository extends QuerydslRepositorySupport {
	@PersistenceContext
	private final EntityManager entityManager;
	private final JPAQueryFactory queryFactory;

	QBook qBook = QBook.book;
	QReview qReview = QReview.review;
	QLikes qLikes = QLikes.likes;
	QBookStatus qBookStatus = QBookStatus.bookStatus;
	QBookTag qBookTag = QBookTag.bookTag;
	QTag qTag = QTag.tag;
	QBookCategory qBookCategory = QBookCategory.bookCategory;
	QCategory qCategory = QCategory.category;
	QImage qImage = QImage.image;
	QBookImage qBookImage = QBookImage.bookImage;
	QOrdersBook qOrdersBook = QOrdersBook.ordersBook;

	public BookQueryRepository(EntityManager entityManager) {
		super(Book.class);
		this.entityManager = entityManager;
		this.queryFactory = new JPAQueryFactory(entityManager);
	}

	public GetBookResponse getBook(Long id) {


		Book book = from(qBook).leftJoin(qReview)
			.on(qOrdersBook.book.id.eq(qReview.ordersBook.book.id))
			.leftJoin(qLikes)
			.on(qBook.id.eq(qLikes.book.id))
			.join(qBookStatus)
			.on(qBook.bookStatus.id.eq(qBookStatus.id))
			.where(qBook.bookStatus.id.ne(4L).and(qBook.id.eq(id)))
			.fetchOne();
		if (book == null) {
			throw new NotFoundException(ErrorCode.BOOK_NOT_FOUND);
		}

		// BookTag 및 BookCategory 서브쿼리로 조회
		List<String> tags = from(qBookTag).join(qTag)
			.on(qBookTag.tag.id.eq(qTag.id))
			.where(qBookTag.book.id.eq(book.getId()))
			.select(qTag.name)
			.fetch();

		List<String> categories = from(qBookCategory).join(qCategory)
			.on(qBookCategory.category.id.eq(qCategory.id))
			.where(qBookCategory.book.id.eq(book.getId()))
			.select(qCategory.name)
			.fetch();

		long likesCount = from(qLikes).where(qLikes.book.id.eq(book.getId())).fetchCount();

		// Review 조회
		Double score = from(qReview).where(qReview.ordersBook.book.id.eq(book.getId()))
			.select(qReview.score.avg().coalesce(0.0))
			.fetchFirst();

		Image image = from(qBookImage).join(qImage)
			.on(qBookImage.image.id.eq(qImage.id))
			.where(qBookImage.book.id.eq(book.getId()))
			.select(qImage)
			.fetchOne();

		return GetBookResponse.fromEntity(book, tags, categories, (int)likesCount, score, image);

	}

	public List<BookDocument> getBookDocuments(Pageable pageable) {
		JPAQuery<GetBookSearchResponse> query = new JPAQuery<>(entityManager)
			.select(Projections.constructor(GetBookSearchResponse.class,
				qBook.id,
				qBook.title,
				qBook.author,
				qBook.publisher,
				qImage.source,
				qBook.price,
				qBook.discountPrice,
				qReview.score.avg().coalesce(0.0),
				qBook.isPackaged
			))
			.from(qBook)
			.leftJoin(qBookImage).on(qBook.id.eq(qBookImage.book.id))
			.leftJoin(qImage).on(qBookImage.image.id.eq(qImage.id))
			.leftJoin(qReview).on(qBook.id.eq(qReview.ordersBook.book.id))
			.where(qBook.bookStatus.id.ne(4L)) // Assuming 4 is the ID for 'deleted' book status
			.groupBy(qBook.id, qBook.title, qBook.author, qBook.publisher, qImage.source, qBook.price, qBook.discountPrice, qBook.isPackaged);

		// Apply pagination
		List<GetBookSearchResponse> results = query.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		List<BookDocument> bookDocumentList = new ArrayList<>();

		// Get IDs from GetBookSearchResponse list
		List<Long> bookIds = results.stream()
			.map(GetBookSearchResponse::id)
			.collect(Collectors.toList());

		// Fetch tag names for all books
		List<Tuple> tagTuples = queryFactory
			.select(qBookTag.book.id, qTag.name )
			.from(qBookTag)
			.join(qTag).on(qBookTag.tag.id.eq(qTag.id))
			.where(qBookTag.book.id.in(bookIds))
			.fetch();

		// Fetch category names for all books
		List<Tuple> categoryTuples = queryFactory.select(qBookCategory.book.id, qCategory.name)
			.from(qBookCategory)
			.join(qCategory).on(qBookCategory.category.id.eq(qCategory.id))
			.where(qBookCategory.book.id.in(bookIds))
			.fetch();

		// Prepare tag and category maps for efficient lookup
		Map<Long, List<String>> tagMap = new HashMap<>();
		Map<Long, List<String>> categoryMap = new HashMap<>();
		Map<Long, Long> reviewCountMap = new HashMap<>();

		// Fill tag map
		for (Tuple tuple : tagTuples) {
			Long bookId = tuple.get(qBookTag.book.id);
			String tagName = tuple.get(qTag.name);
			tagMap.computeIfAbsent(bookId, k -> new ArrayList<>()).add(tagName);
		}

		// Fill category map
		for (Tuple tuple : categoryTuples) {
			Long bookId = tuple.get(qBookCategory.book.id);
			String categoryName = tuple.get(qCategory.name);
			categoryMap.computeIfAbsent(bookId, k -> new ArrayList<>()).add(categoryName);
		}

		// Populate BookDocument list
		for (GetBookSearchResponse getBook : results) {
			Long bookId = getBook.id();
			List<String> tagNames = tagMap.getOrDefault(bookId, Collections.emptyList());
			List<String> categoryNames = categoryMap.getOrDefault(bookId, Collections.emptyList());
			Long reviewCount = reviewCountMap.getOrDefault(bookId, 0L); // Default to 0 if review count not found

			bookDocumentList.add(BookDocument.of(getBook, tagNames, categoryNames, reviewCount.intValue()));
		}

		return bookDocumentList;
	}
	public GetBookToMainResponseMap getBookToMainPage() {
		List<GetBookToMainResponse> newBooks = new JPAQuery<>(entityManager)
			.select(Projections.constructor(GetBookToMainResponse.class,
				qBook.id,
				qBook.title,
				qImage.source.as("image"),
				Expressions.numberTemplate(Integer.class, "{0}", qBook.price).as("price"),
				Expressions.numberTemplate(Integer.class, "{0}", qBook.discountPrice).as("discountPrice")
			))
			.from(qBook)
			.leftJoin(qBookImage).on(qBook.id.eq(qBookImage.book.id))
			.leftJoin(qImage).on(qBookImage.image.id.eq(qImage.id))
			.where(qBook.bookStatus.id.ne(4L))
			.orderBy(qBook.createdAt.desc())
			.limit(8)
			.fetch();

		List<GetBookToMainResponse> popularBooks = new JPAQuery<>(entityManager)
			.select(Projections.constructor(GetBookToMainResponse.class,
				qBook.id,
				qBook.title,
				qImage.source.as("image"),
				Expressions.numberTemplate(Integer.class, "{0}", qBook.price).as("price"),
				Expressions.numberTemplate(Integer.class, "{0}", qBook.discountPrice).as("discountPrice")
			))
			.from(qBook)
			.leftJoin(qBookImage).on(qBook.id.eq(qBookImage.book.id))
			.leftJoin(qImage).on(qBookImage.image.id.eq(qImage.id))
			.leftJoin(qLikes).on(qBook.id.eq(qLikes.book.id))
			.where(qBook.bookStatus.id.ne(4L))
			.orderBy(qLikes.count().desc())
			.groupBy(qBook.id, qBook.title, qImage.source, qBook.price, qBook.discountPrice)
			.limit(8)
			.fetch();

		Map<String, List<GetBookToMainResponse>> mainBookData = new HashMap<>();
		mainBookData.put("newBooks", newBooks);
		mainBookData.put("popularBooks", popularBooks);

		return new GetBookToMainResponseMap(mainBookData);

	}

}
