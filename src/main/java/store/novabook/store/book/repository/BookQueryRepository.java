package store.novabook.store.book.repository;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.book.dto.response.GetBookResponse;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.QBook;
import store.novabook.store.book.entity.QBookStatus;
import store.novabook.store.book.entity.QLikes;
import store.novabook.store.book.entity.QReview;
import store.novabook.store.category.entity.Category;
import store.novabook.store.category.entity.QBookCategory;
import store.novabook.store.category.entity.QCategory;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.image.entity.Image;
import store.novabook.store.image.entity.QBookImage;
import store.novabook.store.image.entity.QImage;
import store.novabook.store.orders.entity.QOrdersBook;
import store.novabook.store.tag.entity.QBookTag;
import store.novabook.store.tag.entity.QTag;

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
		QImage qImage = QImage.image;
		QBookImage qBookImage = QBookImage.bookImage;


		Book book = from(qBook)
			.leftJoin(qReview).on(qBook.id.eq(qReview.ordersBook.book.id))
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

		Category category = from(qBookCategory)
			.join(qCategory).on(qBookCategory.category.id.eq(qCategory.id))
			.where(qBookCategory.book.id.eq(book.getId()))
			.select(qCategory)
			.fetchOne();

		long likesCount = from(qLikes)
			.where(qLikes.book.id.eq(book.getId()))
			.fetchCount();

		// Review 조회
		Integer score = from(qReview)
			.where(qReview.ordersBook.book.id.eq(book.getId()))
			.select(qReview.score)
			.fetchFirst();

		Image image = from(qBookImage)
			.join(qImage).on(qBookImage.image.id.eq(qImage.id))
			.where(qBookImage.book.id.eq(book.getId()))
			.select(qImage)
			.fetchOne();

		// Review가 없는 경우 기본값 설정
		if (score == null) {
			score = 0;
		}

		return GetBookResponse.fromEntity(book, tags, category, (int)likesCount, score, image);

	}

}
