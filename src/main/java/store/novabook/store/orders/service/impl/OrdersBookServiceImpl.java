package store.novabook.store.orders.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto.response.GetOrdersBookReviewIdResponse;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.orders.dto.request.CreateOrdersBookRequest;
import store.novabook.store.orders.dto.request.UpdateOrdersBookRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetOrdersBookResponse;
import store.novabook.store.orders.entity.Orders;
import store.novabook.store.orders.entity.OrdersBook;
import store.novabook.store.orders.repository.OrdersBookRepository;
import store.novabook.store.orders.repository.OrdersRepository;
import store.novabook.store.orders.service.OrdersBookService;

@Service
@RequiredArgsConstructor
@Transactional
public class OrdersBookServiceImpl implements OrdersBookService {
	private final OrdersBookRepository ordersBookRepository;
	private final OrdersRepository ordersRepository;
	private final BookRepository bookRepository;

	@Override
	public CreateResponse create(CreateOrdersBookRequest request) {
		Orders orders = ordersRepository.findById(request.ordersId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.ORDERS_NOT_FOUND));
		Book book = bookRepository.findById(request.bookId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_NOT_FOUND));
		OrdersBook ordersBook = new OrdersBook(orders, book, request);
		ordersBookRepository.save(ordersBook);
		return new CreateResponse(ordersBook.getId());
	}

	@Override
	public Page<GetOrdersBookResponse> getOrdersBookAll() {
		List<OrdersBook> ordersBooks = ordersBookRepository.findAll();
		List<GetOrdersBookResponse> responses = new ArrayList<>();
		for (OrdersBook ordersBook : ordersBooks) {
			responses.add(GetOrdersBookResponse.from(ordersBook));
		}
		return new PageImpl<>(responses);
	}

	@Override
	public GetOrdersBookResponse getOrdersBook(Long id) {
		OrdersBook ordersBook = ordersBookRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorCode.ORDERS_BOOK_NOT_FOUND));
		return GetOrdersBookResponse.from(ordersBook);
	}

	@Override
	public void update(Long id, UpdateOrdersBookRequest request) {
		Orders orders = ordersRepository.findById(request.ordersId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.ORDERS_NOT_FOUND));
		Book book = bookRepository.findById(request.bookId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_NOT_FOUND));
		OrdersBook ordersBook = ordersBookRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorCode.ORDERS_BOOK_NOT_FOUND));

		// TODO: UPDATE 구현해주세요.
		// ordersBook.update(orders, book, request);
	}

	@Override
	public void delete(Long id) {
		ordersBookRepository.deleteById(id);
	}

	//마이페이지에 주문한 책목록 리뷰 달수 있게 목록 전달
	@Override
	public Page<GetOrdersBookReviewIdResponse> getOrdersBookReviewByMemberId(Long memberId, Pageable pageable) {
		return ordersBookRepository.getOrdersBookReviewIdByMemberId(memberId, pageable);
	}
}
