package store.novabook.store.orders.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
import store.novabook.store.orders.entity.DeliveryFee;
import store.novabook.store.orders.entity.Orders;
import store.novabook.store.orders.entity.OrdersBook;
import store.novabook.store.orders.entity.OrdersStatus;
import store.novabook.store.orders.entity.WrappingPaper;
import store.novabook.store.orders.repository.DeliveryFeeRepository;
import store.novabook.store.orders.repository.OrdersBookRepository;
import store.novabook.store.orders.repository.OrdersRepository;
import store.novabook.store.orders.repository.OrdersStatusRepository;
import store.novabook.store.orders.repository.RedisOrderNonMemberRepository;
import store.novabook.store.orders.repository.RedisOrderRepository;
import store.novabook.store.orders.repository.WrappingPaperRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrdersRabbitServiceImpl {

	private final OrdersRepository ordersRepository;
	private final DeliveryFeeRepository deliveryFeeRepository;
	private final WrappingPaperRepository wrappingPaperRepository;
	private final OrdersStatusRepository ordersStatusRepository;
	private final MemberRepository memberRepository;
	private final BookRepository bookRepository;
	private final BookStatusRepository bookStatusRepository;
	private final OrdersBookRepository ordersBookRepository;

	private final RedisOrderRepository redisOrderRepository;
	private final RedisOrderNonMemberRepository redisOrderNonMemberRepository;
	private final RabbitTemplate rabbitTemplate;
	public static final String NOVA_ORDERS_SAGA_EXCHANGE = "nova.orders.saga.exchange";

	/**
	 * redis에 저장된 가주문 폼 정보를 검증하는 로직
	 * - 회원은 memberID로 조회
	 * - 비회원은 orderUUID로 조회
	 * @param orderSagaMessage
	 */
	@Transactional
	@RabbitListener(queues = "nova.orders.form.verify.queue")
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
		} finally {
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "nova.api1-producer-routing-key", orderSagaMessage);
		}
	}

	/**
	 * 비회원 주문 폼을 조회하는 메서드
	 * @param orderSagaMessage
	 */
	private void processNonMemberOrder(OrderSagaMessage orderSagaMessage) {
		Optional<OrderTemporaryNonMemberForm> repository = redisOrderNonMemberRepository.findById(
			orderSagaMessage.getPaymentRequest().orderId());

		if(repository.isEmpty()) {
			throw new IllegalArgumentException("주문 정보가 조회되지 않습니다");
		}

		OrderTemporaryNonMemberForm orderForm = repository.get();
		setOrderSagaMessageFlags(orderSagaMessage, orderForm.usePointAmount(), orderForm.couponId());

		List<BookIdAndQuantityDTO> books = orderForm.books();
		processBooksConfirm(books, orderSagaMessage, orderForm.deliveryId(), orderForm.wrappingPaperId());
	}

	/**
	 * 회원 주문 폼을 조회하는 메서드
	 * @param orderSagaMessage
	 * @param memberId
	 */
	private void processMemberOrder(OrderSagaMessage orderSagaMessage, Long memberId) {
		Optional<OrderTemporaryForm> orderFormOptional = redisOrderRepository.findById(memberId);
		if (orderFormOptional.isEmpty()) {
			throw new IllegalArgumentException("주문 정보가 없습니다.");
		}

		OrderTemporaryForm orderForm = orderFormOptional.get();
		setOrderSagaMessageFlags(orderSagaMessage, orderForm.usePointAmount(), orderForm.couponId());

		List<BookIdAndQuantityDTO> books = orderForm.books();
		processBooksConfirm(books ,orderSagaMessage, orderForm.deliveryId(), orderForm.wrappingPaperId());
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

	/**
	 * 실제 검증이 이뤄지는 메서드
	 * 순수 금액 계산도 저장함
	 * 모든 검증을 마치고 재고 감소 -> 조회 2번 필요
	 * - bookCache를 사용해서 한번만 조회
	 * @param books
	 * @param orderSagaMessage
	 * @param deliveryId
	 * @param wrappingPaperId
	 */
	private void processBooksConfirm(List<BookIdAndQuantityDTO> books, OrderSagaMessage orderSagaMessage, Long deliveryId, Long wrappingPaperId) {
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

			// 계산해야할 금액 저장 (쿠폰, 포인트 감소 미적용)
			long bookPrice = orderSagaMessage.getCalculateTotalAmount() + (book.getPrice() - book.getDiscountPrice()) * bookDTO.quantity(); //순수금액
			orderSagaMessage.setBookAmount(bookPrice);

			if( deliveryFeeRepository.findById(deliveryId).isEmpty() || wrappingPaperRepository.findById(wrappingPaperId).isEmpty()) {
				throw new IllegalArgumentException("배달비 정책, 포장지 정보를 조회할 수 없습니다");
			}

			DeliveryFee deliveryFee = deliveryFeeRepository.findById(deliveryId).get();
			WrappingPaper wrappingPaper = wrappingPaperRepository.findById(wrappingPaperId).get();
			orderSagaMessage.setCalculateTotalAmount(bookPrice + deliveryFee.getFee() + wrappingPaper.getPrice());

			if(bookPrice <= 0) {
				throw new IllegalArgumentException("할인이 적용된 가격이 0원보다 낮습니다");
			}

			log.info("현재 계산 금액 : {}", orderSagaMessage.getCalculateTotalAmount());

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
	 * 재고 증가
	 * 만약 도서 수량이 많으면 도서 상태 변경
	 */
	@RabbitListener(queues = "nova.orders.compensate.form.confirm.queue")
	public void compensateConfirmOrderForm(@Payload OrderSagaMessage orderSagaMessage) {
		try {
			List<BookIdAndQuantityDTO> books;

			// 비회원일 때
			if (orderSagaMessage.getPaymentRequest().memberId() == null) {
				Optional<OrderTemporaryNonMemberForm> optionalForm = redisOrderNonMemberRepository.findById(
					orderSagaMessage.getPaymentRequest().orderId());

				if (optionalForm.isEmpty()) {
					throw new IllegalArgumentException("주문 정보를 찾을 수 없습니다");
				}

				OrderTemporaryNonMemberForm orderTemporaryNonMemberForm = optionalForm.get();
				books = orderTemporaryNonMemberForm.books();
			} else {
				Optional<OrderTemporaryForm> optionalForm = redisOrderRepository.findById(
					orderSagaMessage.getPaymentRequest().memberId());

				if (optionalForm.isEmpty()) {
					throw new IllegalArgumentException("주문 정보를 찾을 수 없습니다");
				}

				OrderTemporaryForm orderTemporaryForm = optionalForm.get();
				books = orderTemporaryForm.books();
			}

			for (BookIdAndQuantityDTO bookDTO : books) {
				Optional<Book> optionalBook = bookRepository.findById(bookDTO.id());
				if (optionalBook.isEmpty()) {
					throw new IllegalArgumentException("해당 도서가 존재하지 않습니다: " + bookDTO.id());
				}

				Book book = optionalBook.get();
				book.increaseInventory((int) bookDTO.quantity());

				// 책의 상태를 FOR_SALE 변경
				if (book.getInventory() > 0) {
					Optional<BookStatus> statusOptional = bookStatusRepository.findById(BookStatusEnum.FOR_SALE.getValue());
					if (statusOptional.isEmpty()) {
						throw new IllegalArgumentException("책 상태를 찾을 수 없습니다.");
					}
					book.setBookStatus(statusOptional.get());
				}

				bookRepository.save(book);
			}
		} catch (Exception e) {
			orderSagaMessage.setStatus("FAIL_COMPENSATE_CONFIRM_ORDER_FORM");
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "nova.orders.saga.dead.routing.key", orderSagaMessage);
		}
	}

	/**
	 * 결제까지 완료되어 Database에 저장하는 메서드
	 * @param orderSagaMessage
	 */
	@RabbitListener(queues = "nova.orders.save.orders.database.queue")
	@Transactional
	public void saveSagaOrder(@Payload OrderSagaMessage orderSagaMessage) {
		try {
			CreateOrdersRequest request;
			Orders orders;
			Orders save;
			List<BookIdAndQuantityDTO> books;

			// 비회원일 경우
			if (orderSagaMessage.getPaymentRequest().memberId() == null) {
				OrderTemporaryNonMemberForm orderForm = getOrderTemporaryNonMemberForm(
					orderSagaMessage.getPaymentRequest().orderId());
				request = createOrdersRequestForNonMember(orderSagaMessage, orderForm);
				orders = createOrderForNonMember(request, orderForm);
				save = ordersRepository.save(orders);
				books = orderForm.books();

			} else {
				OrderTemporaryForm orderForm = getOrderTemporaryForm(orderSagaMessage.getPaymentRequest().memberId());
				request = createOrdersRequestForMember(orderSagaMessage, orderForm);
				orders = createOrderForMember(request, orderForm, orderSagaMessage.getPaymentRequest().memberId());
				save = ordersRepository.save(orders);
				books = orderForm.books();
			}

			// DB 저장
			saveOrdersBooks(save, books);
			orderSagaMessage.setStatus("SUCCESS_SAVE_ORDERS_DATABASE");
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			orderSagaMessage.setStatus("FAIL_SAVE_ORDERS_DATABASE");
		} finally {
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "nova.api5-producer-routing-key", orderSagaMessage);
		}
	}

	/**
	 * OrderBook 테이블을 저장
	 * @param save
	 * @param books
	 */
	private void saveOrdersBooks(Orders save, List<BookIdAndQuantityDTO> books) {
		for (BookIdAndQuantityDTO bookDto : books) {
			Book book = bookRepository.findById(bookDto.id()).get();
			OrdersBook ordersBook = new OrdersBook(save, book, (int) bookDto.quantity(), book.getPrice());
			ordersBookRepository.save(ordersBook);
		}
	}


	private OrderTemporaryNonMemberForm getOrderTemporaryNonMemberForm(UUID orderId) {
		return redisOrderNonMemberRepository.findById(orderId)
			.orElseThrow(() -> new IllegalArgumentException("주문 정보를 조회할 수 없습니다"));
	}

	private OrderTemporaryForm getOrderTemporaryForm(Long memberId) {
		return redisOrderRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("주문 정보를 조회할 수 없습니다"));
	}

	private CreateOrdersRequest createOrdersRequestForNonMember(OrderSagaMessage orderSagaMessage, OrderTemporaryNonMemberForm orderForm) {
		return CreateOrdersRequest.builder()
			.memberId(null)
			.totalAmount(orderSagaMessage.getCalculateTotalAmount())
			.ordersDate(LocalDateTime.now())
			.deliveryAddress(orderForm.orderReceiverInfo().orderAddressInfo().streetAddress() +
				orderForm.orderReceiverInfo().orderAddressInfo().detailAddress())
			.deliveryDate(orderForm.deliveryDate().atTime(00, 00))
			.receiverName(orderForm.orderReceiverInfo().name())
			.receiverNumber(orderForm.orderReceiverInfo().phone())
			.bookPurchaseAmount(1000L)
			.build();
	}

	private CreateOrdersRequest createOrdersRequestForMember(OrderSagaMessage orderSagaMessage, OrderTemporaryForm orderForm) {
		return CreateOrdersRequest.builder()
			.memberId(orderSagaMessage.getPaymentRequest().memberId())
			.totalAmount(orderSagaMessage.getCalculateTotalAmount())
			.ordersDate(LocalDateTime.now())
			.deliveryAddress(orderForm.orderReceiverInfo().orderAddressInfo().streetAddress() +
				orderForm.orderReceiverInfo().orderAddressInfo().detailAddress())
			.deliveryDate(orderForm.deliveryDate().atTime(00, 00))
			.receiverName(orderForm.orderReceiverInfo().name())
			.receiverNumber(orderForm.orderReceiverInfo().phone())
			.bookPurchaseAmount(1000L)
			.build();
	}

	private Orders createOrderForNonMember(CreateOrdersRequest request, OrderTemporaryNonMemberForm orderForm) {
		DeliveryFee deliveryFee = deliveryFeeRepository.findById(orderForm.deliveryId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.DELIVERY_FEE_NOT_FOUND));
		WrappingPaper wrappingPaper = wrappingPaperRepository.findById(orderForm.wrappingPaperId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.WRAPPING_PAPER_NOT_FOUND));
		OrdersStatus ordersStatus = ordersStatusRepository.findById(1L)
			.orElseThrow(() -> new NotFoundException(ErrorCode.ORDERS_STATUS_NOT_FOUND));
		return new Orders(null, deliveryFee, wrappingPaper, ordersStatus, request);
	}

	private Orders createOrderForMember(CreateOrdersRequest request, OrderTemporaryForm orderForm, Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
		DeliveryFee deliveryFee = deliveryFeeRepository.findById(orderForm.deliveryId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.DELIVERY_FEE_NOT_FOUND));
		WrappingPaper wrappingPaper = wrappingPaperRepository.findById(orderForm.wrappingPaperId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.WRAPPING_PAPER_NOT_FOUND));
		OrdersStatus ordersStatus = ordersStatusRepository.findById(1L)
			.orElseThrow(() -> new NotFoundException(ErrorCode.ORDERS_STATUS_NOT_FOUND));
		return new Orders(member, deliveryFee, wrappingPaper, ordersStatus, request);
	}
}
