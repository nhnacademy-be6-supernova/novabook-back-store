package store.novabook.store.cart.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.QBook;
import store.novabook.store.book.entity.QBookStatus;
import store.novabook.store.cart.dto.CartBookIdDTO;
import store.novabook.store.cart.dto.request.CreateCartBookRequest;
import store.novabook.store.cart.dto.CartBookDTO;
import store.novabook.store.cart.dto.CartBookListDTO;
import store.novabook.store.cart.entity.Cart;
import store.novabook.store.cart.entity.CartBook;
import store.novabook.store.cart.entity.QCart;
import store.novabook.store.cart.entity.QCartBook;
import store.novabook.store.common.exception.BadRequestException;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
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
	QBookStatus qBookStatus = QBookStatus.bookStatus;


	public CartQueryRepository(EntityManager entityManager) {
		super(Cart.class);
		this.entityManager = entityManager;
		this.queryFactory = new JPAQueryFactory(entityManager);
	}


	public CartBookListDTO getCartBookAll(Long cartId) {
		List<CartBookDTO> cartBookResponses = queryFactory
			.select(Projections.constructor(CartBookDTO.class,
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
			.join(qBookStatus).on(qBookStatus.id.eq(qBook.bookStatus.id))
			.where(qCart.id.eq(cartId)
				.and(qBook.bookStatus.id.ne(4L)))
			.fetch();

		// 결과값이 없으면 빈 리스트 반환
		if (cartBookResponses == null || cartBookResponses.isEmpty()) {
			return new CartBookListDTO(Collections.emptyList());
		}

		return new CartBookListDTO(cartBookResponses);
	}

	public CartBookListDTO getCartBookAllByMemberId(Long memberId) {
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

			return new CartBookListDTO(Collections.emptyList());
		}

		List<CartBookDTO> cartBookResponses = queryFactory
			.select(Projections.constructor(CartBookDTO.class,
				qBook.id,
				qBook.title,
				qImage.source,
				qBook.price,
				qBook.discountPrice,
				// CASE 문을 사용하여 quantity를 설정
				Expressions.cases()
					.when(qCartBook.quantity.gt(qBook.inventory))
					.then(qBook.inventory)
					.otherwise(qCartBook.quantity),
				qBook.isPackaged,
				qBook.bookStatus.id))
			.from(qCart)
			.join(qMember).on(qCart.member.id.eq(qMember.id))
			.join(qCartBook).on(qCart.id.eq(qCartBook.cart.id))
			.join(qBook).on(qCartBook.book.id.eq(qBook.id))
			.join(qBookImage).on(qBook.id.eq(qBookImage.book.id))
			.join(qImage).on(qBookImage.image.id.eq(qImage.id))
			.where(qMember.id.eq(memberId)
				.and(qCartBook.isExposed.eq(true)))
			.fetch();


		// 결과값이 없으면 빈 리스트 반환
		if (cartBookResponses == null || cartBookResponses.isEmpty()) {
			return new CartBookListDTO(Collections.emptyList());
		}

		return new CartBookListDTO(cartBookResponses);


	}

	public CartBook createCartBook(Long memberId, CartBookDTO createCartBookRequest) {
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
			throw new NotFoundException(ErrorCode.BOOK_NOT_FOUND);
		}

		// 추가하려는 수량이 재고 수량보다 큰 경우 예외 발생
		if (createCartBookRequest.quantity() > book.getInventory()) {
			throw new BadRequestException(ErrorCode.NOT_UPDATE_CART_QUANTITY);
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
			int newQuantity = cartBook.getQuantity() + createCartBookRequest.quantity();

			// 추가하려는 수량이 재고 수량보다 큰 경우 예외 발생
			if (newQuantity > book.getInventory()) {
				throw new BadRequestException(ErrorCode.NOT_UPDATE_CART_QUANTITY);
			}

			cartBook.updateQuantity(newQuantity);
		} else {
			cartBook = CartBook.of(cart, book, createCartBookRequest.quantity());
		}

		return cartBook;
	}

	public CartBookListDTO getCartBookAllGuest(CartBookIdDTO request) {
		Map<Long, Integer> bookIdsAndQuantity = request.bookIdsAndQuantity();
		List<Long> bookIds = new ArrayList<>(bookIdsAndQuantity.keySet());

		// Book 엔티티와 관련된 정보를 가져옵니다.
		List<CartBookDTO> newCartBooks = queryFactory
			.select(Projections.constructor(CartBookDTO.class,
				qBook.id,
				qBook.title,
				qImage.source,
				qBook.price,
				qBook.discountPrice,
				qBook.inventory.as("quantity"),// 실제 재고 수량을 가져옵니다.
				qBook.isPackaged,
				qBook.bookStatus.id))
			.from(qBook)
			.join(qBookImage).on(qBook.id.eq(qBookImage.book.id))
			.join(qImage).on(qBookImage.image.id.eq(qImage.id))
			.where(qBook.id.in(bookIds)
				.and(qBook.bookStatus.id.ne(4L))) // bookStatus가 4가 아닌 경우만 가져옵니다.
			.fetch();

		// 실제 수량이 재고보다 큰 경우 재고 수량으로 변경
		List<CartBookDTO> adjustedCartBooks = new ArrayList<>();
		for (CartBookDTO cartBook : newCartBooks) {
			Integer requestedQuantity = bookIdsAndQuantity.getOrDefault(cartBook.bookId(), 1);
			Integer adjustedQuantity = Math.min(requestedQuantity, cartBook.quantity());
			CartBookDTO adjustedCartBook = new CartBookDTO(
				cartBook.bookId(),
				cartBook.title(),
				cartBook.image(),
				cartBook.price(),
				cartBook.discountPrice(),
				adjustedQuantity,
				cartBook.isPackaged(),
				cartBook.bookStatusId()// 재고 수량도 함께 포함
			);
			adjustedCartBooks.add(adjustedCartBook);
		}

		return new CartBookListDTO(adjustedCartBooks);

	}
}

