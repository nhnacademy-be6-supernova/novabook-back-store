package store.novabook.store.cart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

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
import store.novabook.store.member.entity.QMember;

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


	public GetCartResponse getCartBookAll(Long cartId) {
		QCart qCart = QCart.cart;
		QCartBook qCartBook = QCartBook.cartBook;
		QBook qBook = QBook.book;
		QBookImage qBookImage = QBookImage.bookImage;
		QImage qImage = QImage.image;
		QMember qMember = QMember.member;

		List<GetCartBookResponse> cartBookResponses = queryFactory
			.select(Projections.constructor(GetCartBookResponse.class,
				qBook.id,
				qBook.title,
				qImage.source,
				qBook.price,
				qBook.discountPrice,
				qCartBook.quantity))
			.from(qCart)
			.join(qMember).on(qCart.member.id.eq(qMember.id))
			.join(qCartBook).on(qCart.id.eq(qCartBook.cart.id))
			.join(qBook).on(qCartBook.book.id.eq(qBook.id))
			.join(qBookImage).on(qBook.id.eq(qBookImage.book.id))
			.join(qImage).on(qBookImage.image.id.eq(qImage.id))
			.where(qCart.id.eq(cartId))
			.fetch();

		return new GetCartResponse(cartId, cartBookResponses);
	}

	public GetCartResponse getCartBookAllByMemberId(Long memberId) {
		QCart qCart = QCart.cart;
		QCartBook qCartBook = QCartBook.cartBook;
		QBook qBook = QBook.book;
		QBookImage qBookImage = QBookImage.bookImage;
		QImage qImage = QImage.image;
		QMember qMember = QMember.member;

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

		List<GetCartBookResponse> cartBookResponses = queryFactory
			.select(Projections.constructor(GetCartBookResponse.class,
				qBook.id,
				qBook.title,
				qImage.source,
				qBook.price,
				qBook.discountPrice,
				qCartBook.quantity))
			.from(qCart)
			.join(qMember).on(qCart.member.id.eq(qMember.id))
			.join(qCartBook).on(qCart.id.eq(qCartBook.cart.id))
			.join(qBook).on(qCartBook.book.id.eq(qBook.id))
			.join(qBookImage).on(qBook.id.eq(qBookImage.book.id))
			.join(qImage).on(qBookImage.image.id.eq(qImage.id))
			.where(qMember.id.eq(memberId))
			.fetch();

		return new GetCartResponse(cartId, cartBookResponses);
	}
}
