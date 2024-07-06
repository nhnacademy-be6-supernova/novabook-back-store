package store.novabook.store.cart.repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.QBook;
import store.novabook.store.cart.dto.request.CreateCartBookRequest;
import store.novabook.store.cart.dto.response.CreateCartBookResponse;
import store.novabook.store.cart.dto.response.GetCartBookResponse;
import store.novabook.store.cart.dto.response.GetCartResponse;
import store.novabook.store.cart.entity.Cart;
import store.novabook.store.cart.entity.CartBook;
import store.novabook.store.cart.entity.QCart;
import store.novabook.store.cart.entity.QCartBook;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.image.entity.QBookImage;
import store.novabook.store.image.entity.QImage;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.entity.QMember;

@Repository
public class CartQueryRepository extends QuerydslRepositorySupport {
	@PersistenceContext
	private final EntityManager entityManager;
	private final JPAQueryFactory queryFactory;

	QCart qCart = QCart.cart;
	QCartBook qCartBook = QCartBook.cartBook;
	QBook qBook = QBook.book;
	QBookImage qBookImage = QBookImage.bookImage;
	QImage qImage = QImage.image;
	QMember qMember = QMember.member;


	public CartQueryRepository(EntityManager entityManager) {
		super(Cart.class);
		this.entityManager = entityManager;
		this.queryFactory = new JPAQueryFactory(entityManager);
	}


	public GetCartResponse getCartBookAll(Long cartId) {
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

			return new GetCartResponse(newCart.getId(), Collections.emptyList());
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

	public CartBook createCartBook(Long memberId, CreateCartBookRequest createCartBookRequest) {
		// 회원 ID로 Cart 조회
		Cart cart = queryFactory.selectFrom(qCart)
			.where(qCart.member.id.eq(memberId))
			.fetchOne();

		if (cart == null) {
			Member member = entityManager.getReference(Member.class, memberId);
			Cart newCart = Cart.of(member);

			entityManager.persist(newCart);
			entityManager.flush();

			cart= newCart;
		}

		// Book 조회
		Book book = queryFactory.selectFrom(qBook)
			.where(qBook.id.eq(createCartBookRequest.bookId()))
			.fetchOne();

		if (book == null) {
			throw new EntityNotFoundException(Book.class, createCartBookRequest.bookId());
		}

		// CartBook 조회
		Optional<CartBook> cartBookOptional = Optional.ofNullable(
			queryFactory.selectFrom(qCartBook)
				.where(qCartBook.cart.id.eq(cart.getId())
					.and(qCartBook.book.id.eq(createCartBookRequest.bookId()))
					.and(qCartBook.isExposed.eq(true)))
				.fetchOne()
		);

		// CartBook 업데이트 또는 새로 추가
		CartBook cartBook;
		if (cartBookOptional.isPresent()) {
			cartBook = cartBookOptional.get();
			cartBook.updateQuantity(cartBook.getQuantity() + createCartBookRequest.quantity());
		} else {
			cartBook = CartBook.of(cart, book, createCartBookRequest.quantity());
		}

		return cartBook;
	}
}

