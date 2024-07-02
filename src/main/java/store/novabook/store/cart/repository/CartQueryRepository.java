package store.novabook.store.cart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import store.novabook.store.book.entity.QBook;
import store.novabook.store.cart.dto.response.GetCartBookResponse;
import store.novabook.store.cart.dto.response.GetCartResponse;
import store.novabook.store.cart.entity.Cart;
import store.novabook.store.cart.entity.QCart;
import store.novabook.store.cart.entity.QCartBook;
import store.novabook.store.image.entity.QBookImage;
import store.novabook.store.image.entity.QImage;
import store.novabook.store.member.entity.Member;

@Repository
public class CartQueryRepository extends QuerydslRepositorySupport {
	@PersistenceContext
	private final EntityManager entityManager;
	private final JPAQueryFactory queryFactory;


	public CartQueryRepository(EntityManager entityManager) {
		super(Cart.class);
		this.entityManager = entityManager;
		this.queryFactory = new JPAQueryFactory(entityManager);
	}


	public GetCartResponse getCartBook(Long memberId) {
		QCart qCart = QCart.cart;
		QCartBook qCartBook = QCartBook.cartBook;
		QBook qBook = QBook.book;
		QBookImage qBookImage = QBookImage.bookImage;
		QImage qImage = QImage.image;

		// 장바구니 ID 조회
		Long cartId = queryFactory
			.select(qCart.id)
			.from(qCart)
			.where(qCart.member.id.eq(memberId))
			.fetchOne();

		if (cartId == null) {
			Member member = entityManager.getReference(Member.class, memberId);
			Cart newCart = Cart.of(member);

			entityManager.persist(newCart);
			entityManager.flush();

			return new GetCartResponse(newCart.getId(), null);
		}

		// 장바구니에 담긴 책 정보 조회
		List<GetCartBookResponse> cartBookResponses = queryFactory
			.select(Projections.constructor(GetCartBookResponse.class,
				qBook.id,
				qImage.source,
				qBook.title,
				qBook.price,
				qBook.discountPrice,
				qCartBook.quantity))
			.from(qCartBook)
			.join(qCartBook.book, qBook)
			.join(qBookImage.image, qImage)
			.join(qBook, qBookImage.book)
			.where(qCartBook.cart.id.eq(cartId))
			.fetch();

		return new GetCartResponse(cartId, cartBookResponses);
	}

}
