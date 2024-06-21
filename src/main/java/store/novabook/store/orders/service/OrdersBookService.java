package store.novabook.store.orders.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.orders.dto.CreateOrdersBookRequest;
import store.novabook.store.orders.dto.CreateResponse;
import store.novabook.store.orders.dto.GetOrdersBookResponse;
import store.novabook.store.orders.dto.UpdateOrdersBookRequest;
import store.novabook.store.orders.entity.Orders;
import store.novabook.store.orders.entity.OrdersBook;
import store.novabook.store.orders.repository.OrdersBookRepository;
import store.novabook.store.orders.repository.OrdersRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class OrdersBookService {
	private final OrdersBookRepository ordersBookRepository;
	private final OrdersRepository ordersRepository;
	private final BookRepository bookRepository;

	public CreateResponse create(CreateOrdersBookRequest request) {
		Orders orders = ordersRepository.findById(request.ordersId())
			.orElseThrow(() -> new EntityNotFoundException(Orders.class, request.ordersId()));
		Book book = bookRepository.findById(request.bookId())
			.orElseThrow(() -> new EntityNotFoundException(Book.class, request.bookId()));
		OrdersBook ordersBook = new OrdersBook(orders, book, request);
		ordersBookRepository.save(ordersBook);
		return new CreateResponse(ordersBook.getId());
	}

	//전체 조회
	public Page<GetOrdersBookResponse> getOrdersBookAll() {
		List<OrdersBook> ordersBooks = ordersBookRepository.findAll();
		List<GetOrdersBookResponse> responses = new ArrayList<>();
		for (OrdersBook ordersBook : ordersBooks) {
			responses.add(GetOrdersBookResponse.from(ordersBook));
		}
		return new PageImpl<>(responses);
	}

	// 단건조회
	public GetOrdersBookResponse getOrdersBook(Long id) {
		OrdersBook ordersBook = ordersBookRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(OrdersBook.class, id));
		return GetOrdersBookResponse.from(ordersBook);
	}

	//업데이트
	public void update(Long id, UpdateOrdersBookRequest request) {
		Orders orders = ordersRepository.findById(request.ordersId())
			.orElseThrow(() -> new EntityNotFoundException(Orders.class, request.ordersId()));
		Book book = bookRepository.findById(request.bookId())
			.orElseThrow(() -> new EntityNotFoundException(Book.class, request.bookId()));
		OrdersBook ordersBook = ordersBookRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(OrdersBook.class, id));
		ordersBook.update(orders, book, request);
	}

	public void delete(Long id) {
		ordersBookRepository.deleteById(id);
	}
}
