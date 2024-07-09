package store.novabook.store.orders.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.BookStatus;
import store.novabook.store.book.entity.BookStatusEnum;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.book.repository.BookStatusRepository;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.repository.MemberRepository;
import store.novabook.store.orders.dto.OrderSagaMessage;
import store.novabook.store.orders.dto.request.BookIdAndQuantityDTO;
import store.novabook.store.orders.dto.request.CreateOrdersRequest;
import store.novabook.store.orders.dto.request.OrderTemporaryForm;
import store.novabook.store.orders.dto.request.OrderTemporaryNonMemberForm;
import store.novabook.store.orders.dto.request.UpdateOrdersRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetOrdersResponse;
import store.novabook.store.orders.entity.DeliveryFee;
import store.novabook.store.orders.entity.Orders;
import store.novabook.store.orders.entity.OrdersStatus;
import store.novabook.store.orders.entity.WrappingPaper;
import store.novabook.store.orders.repository.DeliveryFeeRepository;
import store.novabook.store.orders.repository.OrdersRepository;
import store.novabook.store.orders.repository.OrdersStatusRepository;
import store.novabook.store.orders.repository.RedisOrderNonMemberRepository;
import store.novabook.store.orders.repository.RedisOrderRepository;
import store.novabook.store.orders.repository.WrappingPaperRepository;
import store.novabook.store.orders.service.OrdersService;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrdersServiceImpl implements OrdersService {

	// 보관용 재고
	private final OrdersRepository ordersRepository;
	private final DeliveryFeeRepository deliveryFeeRepository;
	private final WrappingPaperRepository wrappingPaperRepository;
	private final OrdersStatusRepository ordersStatusRepository;
	private final MemberRepository memberRepository;
	private final BookRepository bookRepository;
	private final BookStatusRepository bookStatusRepository;
	private final RedisOrderRepository redisOrderRepository;
	private final RedisOrderNonMemberRepository redisOrderNonMemberRepository;

	private final RabbitTemplate rabbitTemplate;
	public static final String NOVA_ORDERS_SAGA_EXCHANGE = "nova.orders.saga.exchange";



	@Override
	public CreateResponse create(CreateOrdersRequest request) {
		Member member = memberRepository.findById(request.memberId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
		DeliveryFee deliveryFee = deliveryFeeRepository.findById(request.memberId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.DELIVERY_FEE_NOT_FOUND));
		WrappingPaper wrappingPaper = wrappingPaperRepository.findById(request.memberId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.WRAPPING_PAPER_NOT_FOUND));
		OrdersStatus ordersStatus = ordersStatusRepository.findById(request.memberId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.ORDERS_STATUS_NOT_FOUND));
		Orders orders = new Orders(member, deliveryFee, wrappingPaper, ordersStatus, request);
		ordersRepository.save(orders);
		return new CreateResponse(orders.getId());
	}

	@Override
	public void update(Long id, UpdateOrdersRequest request) {
		Member member = memberRepository.findById(request.memberId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
		DeliveryFee deliveryFee = deliveryFeeRepository.findById(request.memberId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.DELIVERY_FEE_NOT_FOUND));
		WrappingPaper wrappingPaper = wrappingPaperRepository.findById(request.memberId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.WRAPPING_PAPER_NOT_FOUND));
		OrdersStatus ordersStatus = ordersStatusRepository.findById(request.memberId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.ORDERS_STATUS_NOT_FOUND));
		Orders orders = ordersRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorCode.ORDERS_NOT_FOUND));
		orders.update(member, deliveryFee, wrappingPaper, ordersStatus, request);
	}


	@Override
	public Page<GetOrdersResponse> getOrdersResponsesAll() {
		List<Orders> orders = ordersRepository.findAll();
		List<GetOrdersResponse> responses = new ArrayList<>();
		for (Orders order : orders) {
			responses.add(GetOrdersResponse.form(order));
		}
		return new PageImpl<>(responses);
	}

	@Override
	public GetOrdersResponse getOrdersById(Long id) {
		Orders orders = ordersRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorCode.ORDERS_NOT_FOUND));
		return GetOrdersResponse.form(orders);
	}







	/**
	 * @author 2-say
	 * 가주문서 검증 비지니스 로직
	 * 제고 감소도 함께 일어남
	 */
	@Transactional
	@RabbitListener(queues = "nova.orders.form.confirm.queue")
	public void confirmOrderForm(@Payload OrderSagaMessage orderSagaMessage) {
		try {
			Long memberId = orderSagaMessage.getPaymentRequest().memberId();

			if (memberId == null) {
				processNonMemberOrder(orderSagaMessage);
			} else {
				processMemberOrder(orderSagaMessage, memberId);
			}

			orderSagaMessage.setStatus("SUCCESS_CONFIRM_ORDER_FORM");
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			orderSagaMessage.setStatus("FAIL_CONFIRM_ORDER_FORM");
			throw e;
		} finally {
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "nova.api1-producer-routing-key", orderSagaMessage);
		}
	}


	private void processNonMemberOrder(OrderSagaMessage orderSagaMessage) {
		Optional<OrderTemporaryNonMemberForm> repository = redisOrderNonMemberRepository.findById(
			orderSagaMessage.getPaymentRequest().orderId());

		if(repository.isEmpty()) {
			throw new IllegalArgumentException("주문 정보가 조회되지 않습니다");
		}

		OrderTemporaryNonMemberForm orderForm = repository.get();
		setOrderSagaMessageFlags(orderSagaMessage, orderForm.usePointAmount(), orderForm.couponId());

		List<BookIdAndQuantityDTO> books = orderForm.books();
		processBooksConfirm(books, orderSagaMessage);
	}

	private void processMemberOrder(OrderSagaMessage orderSagaMessage, Long memberId) {
		Optional<OrderTemporaryForm> orderFormOptional = redisOrderRepository.findById(memberId);
		if (orderFormOptional.isEmpty()) {
			throw new IllegalArgumentException("주문 정보가 없습니다.");
		}

		OrderTemporaryForm orderForm = orderFormOptional.get();
		setOrderSagaMessageFlags(orderSagaMessage, orderForm.usePointAmount(), orderForm.couponId());

		List<BookIdAndQuantityDTO> books = orderForm.books();
		processBooksConfirm(books ,orderSagaMessage);
	}


	/**
	 * 만약 결제 포인트가 0원 이고, 사용 쿠폰이 없다면, 다음 로직에서 제외하도록 플래그 상태를 설정합니다.
	 * @param orderSagaMessage
	 * @param usePointAmount
	 * @param couponId
	 */
	private void setOrderSagaMessageFlags(OrderSagaMessage orderSagaMessage, long usePointAmount, Long couponId) {
		orderSagaMessage.setNoUsePoint(usePointAmount == 0);
		orderSagaMessage.setNoUseCoupon(couponId == null);
	}


	private void processBooksConfirm(List<BookIdAndQuantityDTO> books, OrderSagaMessage orderSagaMessage) {
		Map<Long, Book> bookCache = new HashMap<>();

		for (BookIdAndQuantityDTO bookDTO : books) {
			Book book = bookCache.computeIfAbsent(bookDTO.id(), id -> {
				Optional<Book> optionalBook = bookRepository.findById(id);
				if (optionalBook.isEmpty()) {
					throw new IllegalArgumentException("해당 도서가 존재하지 않습니다: " + id);
				}
				return optionalBook.get();
			});
			if (!book.getBookStatus().getName().equals(BookStatusEnum.FOR_SALE.getKoreanValue())) {
				throw new IllegalArgumentException("판매중인 도서가 아닙니다: " + book.getId());
			}
		}

		for (BookIdAndQuantityDTO bookDTO : books) {
			Book book = bookCache.get(bookDTO.id());
			book.decreaseInventory((int) bookDTO.quantity());

			// 계산해야할 금액 저장 (할인 미적용)
			long bookPrice = orderSagaMessage.getCalculateTotalAmount() + book.getPrice() * bookDTO.quantity();
			orderSagaMessage.setCalculateTotalAmount(bookPrice);

			if (book.getInventory() <= 0) {
				Optional<BookStatus> statusOptional = bookStatusRepository.findById(BookStatusEnum.OUT_OF_STOCK.getValue());
				if (statusOptional.isEmpty()) {
					throw new IllegalArgumentException("책 상태를 찾을 수 없습니다.");
				}
				book.setBookStatus(statusOptional.get());
			}
			bookRepository.save(book);
		}
	}



	/**
	 * 상위 행위에서 에러 발생 시 보상 트랜잭션 행위
	 */
	// @RabbitListener(queues = "nova.orders.compensate.formConfirm.queue")
	void compensateConfirmOrderForm() {
		// 재고 증가
		// 만약 도서 수량이 많으면 도서 상태 변경
		//
	}


}
