package store.novabook.store.book.repository;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.book.dto.GetBookResponse;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.QBook;
import store.novabook.store.book.entity.QBookStatus;
import store.novabook.store.book.entity.QLikes;
import store.novabook.store.book.entity.QReview;
import store.novabook.store.category.entity.QBookCategory;
import store.novabook.store.category.entity.QCategory;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.tag.entity.QBookTag;
import store.novabook.store.tag.entity.QTag;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class BookQueryRepository extends QuerydslRepositorySupport {

	public BookQueryRepository() {
		super(Book.class);
	}

	public GetBookResponse getBook(Long id) {
		QBook qBook = QBook.book;
		QReview qReview = QReview.review;
		QLikes qLikes = QLikes.likes;
		QBookStatus qBookStatus = QBookStatus.bookStatus;
		QBookTag qBookTag = QBookTag.bookTag;
		QTag qTag = QTag.tag;
		QBookCategory qBookCategory = QBookCategory.bookCategory;
		QCategory qCategory = QCategory.category;

		Book book = from(qBook)
			.leftJoin(qReview).on(qBook.id.eq(qReview.book.id))
			.leftJoin(qLikes).on(qBook.id.eq(qLikes.book.id))
			.join(qBookStatus).on(qBook.bookStatus.id.eq(qBookStatus.id))
			.where(qBook.bookStatus.id.ne(4L).and(qBook.id.eq(id)))
			.fetchOne();
		if (book == null) {
			throw new EntityNotFoundException(Book.class, id);
		}

		// BookTag 및 BookCategory 서브쿼리로 조회
		List<String> tags = from(qBookTag)
			.join(qTag).on(qBookTag.tag.id.eq(qTag.id))
			.where(qBookTag.book.id.eq(book.getId()))
			.select(qTag.name)
			.fetch();

		List<String> categories = from(qBookCategory)
			.join(qCategory).on(qBookCategory.category.id.eq(qCategory.id))
			.where(qBookCategory.book.id.eq(book.getId()))
			.select(qCategory.name)
			.fetch();

		long likesCount = from(qLikes)
			.where(qLikes.book.id.eq(book.getId()))
			.fetchCount();

		// Review 조회
		Integer score = from(qReview)
			.where(qReview.book.id.eq(book.getId()))
			.select(qReview.score)
			.fetchFirst();

		// Review가 없는 경우 기본값 설정
		if (score == null) {
			score = 0;
		}

		return GetBookResponse.builder()
			.id(book.getId())
			.isbn(book.getIsbn())
			.title(book.getTitle())
			.description(book.getDescription())
			.descriptionDetail(book.getDescriptionDetail())
			.bookIndex(book.getBookIndex())
			.author(book.getAuthor())
			.publisher(book.getPublisher())
			.publicationDate(book.getPublicationDate())
			.inventory(book.getInventory())
			.price(book.getPrice())
			.isPackaged(book.isPackaged())
			.image(book.getImage())
			.tags(tags)
			.categories(categories)
			.likes((int)likesCount)
			.discountPrice(book.getDiscountPrice())
			.score(score)
			.build();
	}

}
