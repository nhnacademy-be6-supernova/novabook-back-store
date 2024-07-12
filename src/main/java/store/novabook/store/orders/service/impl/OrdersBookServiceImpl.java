package store.novabook.store.orders.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
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
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetOrderDetailResponse;
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
	public void delete(Long id) {
		ordersBookRepository.deleteById(id);
	}

	//마이페이지에 주문한 책목록 리뷰 달수 있게 목록 전달
	@Override
	public Page<GetOrdersBookReviewIdResponse> getOrdersBookReviewByMemberId(Long memberId, Pageable pageable) {
		return ordersBookRepository.getOrdersBookReviewIdByMemberId(memberId, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetOrdersBookResponse> getOrdersBookByMemberId(Long memberId, Pageable pageable) {
		return ordersBookRepository.getOrdersBookByMemberId(memberId, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public GetOrderDetailResponse getOrderDetail(Long ordersId) {
		List<OrdersBook> ordersBook = ordersBookRepository.getOrderDetailByOrdersId(ordersId);
		return GetOrderDetailResponse.of(ordersBook);
	}

	@Override
	public GetOrderDetailResponse getGuestOrderHistory(Long ordersId) {
		List<OrdersBook> ordersBook = ordersBookRepository.getOrderDetailByOrdersId(ordersId);
		return GetOrderDetailResponse.of(ordersBook);
	}
}
