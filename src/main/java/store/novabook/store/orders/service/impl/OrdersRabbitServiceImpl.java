package store.novabook.store.orders.service.impl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import store.novabook.store.common.exception.BadRequestException;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.entity.MemberGradeHistory;
import store.novabook.store.member.repository.MemberGradeHistoryRepository;
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
import store.novabook.store.point.entity.PointPolicy;
import store.novabook.store.point.repository.PointPolicyRepository;

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
	private final PointPolicyRepository pointPolicyRepository;
	private final RedisOrderRepository redisOrderRepository;
	private final RedisOrderNonMemberRepository redisOrderNonMemberRepository;
	private final RabbitTemplate rabbitTemplate;
	private final MemberGradeHistoryRepository memberGradeHistoryRepository;

	public static final String NOVA_ORDERS_SAGA_EXCHANGE = "nova.orders.saga.exchange";

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

	private void processNonMemberOrder(OrderSagaMessage orderSagaMessage) {
		// 주문폼 조회
		OrderTemporaryNonMemberForm orderForm = getOrderTemporaryNonMemberForm(orderSagaMessage.getPaymentRequest().orderId());
		// 쿠폰, 포인트 사용 여부 설정
		setOrderSagaMessageFlags(orderSagaMessage, orderForm.usePointAmount(), orderForm.couponId());
		// 주문 도서 검증
		processBooksConfirm(orderForm.books(), orderSagaMessage, orderForm.deliveryId(), orderForm.wrappingPaperId());
	}

	private void processMemberOrder(OrderSagaMessage orderSagaMessage, Long memberId) {
		OrderTemporaryForm orderForm = getOrderTemporaryForm(memberId);
		setOrderSagaMessageFlags(orderSagaMessage, orderForm.usePointAmount(), orderForm.couponId());
		processBooksConfirm(orderForm.books(), orderSagaMessage, orderForm.deliveryId(), orderForm.wrappingPaperId());
	}

	private void setOrderSagaMessageFlags(OrderSagaMessage orderSagaMessage, long usePointAmount, Long couponId) {
		orderSagaMessage.setNoEarnPoint(orderSagaMessage.getPaymentRequest().memberId() == null);
		orderSagaMessage.setNoUsePoint(usePointAmount == 0);
		orderSagaMessage.setNoUseCoupon(couponId == null);
	}

	private void processBooksConfirm(List<BookIdAndQuantityDTO> books, OrderSagaMessage orderSagaMessage, Long deliveryId, Long wrappingPaperId) {
		Map<Long, Book> bookCache = new HashMap<>();

		for (BookIdAndQuantityDTO bookDTO : books) {
			Book book = bookCache.computeIfAbsent(bookDTO.id(), id -> bookRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_NOT_FOUND)));

			if (!BookStatusEnum.FOR_SALE.getKoreanValue().equals(book.getBookStatus().getName())) {
				throw new NotFoundException(ErrorCode.BOOK_NOT_SAIL);
			}
		}

		for (BookIdAndQuantityDTO bookDTO : books) {
			Book book = bookCache.get(bookDTO.id());
			book.decreaseInventory((int) bookDTO.quantity());

			// 순수금액 설정
			long bookPrice = orderSagaMessage.getCalculateTotalAmount() +
				(book.getPrice() - book.getDiscountPrice()) * bookDTO.quantity();
			orderSagaMessage.setBookAmount(bookPrice);

			validateDeliveryAndWrapping(deliveryId, wrappingPaperId);

			// 포인트 적립금 계산
			Long memberId = orderSagaMessage.getPaymentRequest().memberId();
			long pointPercent = calculatePointPercent(memberId);
			long earnPointAmount = orderSagaMessage.getBookAmount() * pointPercent;
			orderSagaMessage.setEarnPointAmount(earnPointAmount);

			// 배달피, 포장비를 추가한 금액 산정
			DeliveryFee deliveryFee = deliveryFeeRepository.findById(deliveryId).get();
			WrappingPaper wrappingPaper = wrappingPaperRepository.findById(wrappingPaperId).get();
			orderSagaMessage.setCalculateTotalAmount(bookPrice + deliveryFee.getFee() + wrappingPaper.getPrice());

			if (bookPrice <= 0) {
				throw new BadRequestException(ErrorCode.DISCOUNTED_PRICE_BELOW_ZERO);
			}

			log.info("현재 계산 금액 : {}", orderSagaMessage.getCalculateTotalAmount());

			updateBookStatus(book);
			bookRepository.save(book);
		}
	}

	private void validateDeliveryAndWrapping(Long deliveryId, Long wrappingPaperId) {
		if (deliveryFeeRepository.findById(deliveryId).isEmpty() || wrappingPaperRepository.findById(wrappingPaperId).isEmpty()) {
			throw new NotFoundException(ErrorCode.DELIVERY_FEE_NOT_FOUND);
		}
	}

	/**
	 * 포인트 적립률 계산
	 * @param memberId
	 * @return
	 */
	private long calculatePointPercent(Long memberId) {
		MemberGradeHistory memberGradeHistory = memberGradeHistoryRepository.findFirstByMemberIdOrderByCreatedAtDesc(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_GRADE_HISTORY_NOT_FOUND));
		PointPolicy pointPolicy = pointPolicyRepository.findTopByOrderByCreatedAtDesc()
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_GRADE_POLICY_NOT_FOUND));
		long pointPercent = pointPolicy.getBasicPoint() + (memberGradeHistory.getMemberGradePolicy().getSaveRate() / 100);

		if (pointPercent >= 100) {
			throw new BadRequestException(ErrorCode.INVALID_POINT_DISCOUNT);
		}

		return pointPercent;
	}

	private void updateBookStatus(Book book) {
		if (book.getInventory() <= 0) {
			BookStatus outOfStockStatus = bookStatusRepository.findById(BookStatusEnum.OUT_OF_STOCK.getValue())
				.orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_NOT_FOUND));
			book.setBookStatus(outOfStockStatus);
		}
	}

	@RabbitListener(queues = "nova.orders.compensate.form.confirm.queue")
	public void compensateConfirmOrderForm(@Payload OrderSagaMessage orderSagaMessage) {
		try {
			List<BookIdAndQuantityDTO> books = getOrderBooks(orderSagaMessage);
			compensateBooks(books);
		} catch (Exception e) {
			orderSagaMessage.setStatus("FAIL_COMPENSATE_CONFIRM_ORDER_FORM");
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "nova.orders.saga.dead.routing.key", orderSagaMessage);
		}
	}

	private List<BookIdAndQuantityDTO> getOrderBooks(OrderSagaMessage orderSagaMessage) {
		if (orderSagaMessage.getPaymentRequest().memberId() == null) {
			OrderTemporaryNonMemberForm orderForm = getOrderTemporaryNonMemberForm(orderSagaMessage.getPaymentRequest().orderId());
			return orderForm.books();
		} else {
			OrderTemporaryForm orderForm = getOrderTemporaryForm(orderSagaMessage.getPaymentRequest().memberId());
			return orderForm.books();
		}
	}

	private void compensateBooks(List<BookIdAndQuantityDTO> books) {
		for (BookIdAndQuantityDTO bookDTO : books) {
			Book book = bookRepository.findById(bookDTO.id())
				.orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_NOT_FOUND));
			book.increaseInventory((int) bookDTO.quantity());
			updateBookStatusForCompensation(book);
			bookRepository.save(book);
		}
	}

	private void updateBookStatusForCompensation(Book book) {
		if (book.getInventory() > 0) {
			BookStatus forSaleStatus = bookStatusRepository.findById(BookStatusEnum.FOR_SALE.getValue())
				.orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_NOT_FOUND));
			book.setBookStatus(forSaleStatus);
		}
	}

	@RabbitListener(queues = "nova.orders.save.orders.database.queue")
	public void saveSagaOrder(@Payload OrderSagaMessage orderSagaMessage) {
		try {
			CreateOrdersRequest request;
			Orders orders;
			List<BookIdAndQuantityDTO> books;

			if (orderSagaMessage.getPaymentRequest().memberId() == null) {
				OrderTemporaryNonMemberForm orderForm = getOrderTemporaryNonMemberForm(orderSagaMessage.getPaymentRequest().orderId());
				request = createOrdersRequestForNonMember(orderSagaMessage, orderForm);
				orders = createOrderForNonMember(request, orderForm);
				books = orderForm.books();
			} else {
				OrderTemporaryForm orderForm = getOrderTemporaryForm(orderSagaMessage.getPaymentRequest().memberId());
				request = createOrdersRequestForMember(orderSagaMessage, orderForm);
				orders = createOrderForMember(request, orderForm, orderSagaMessage.getPaymentRequest().memberId());
				books = orderForm.books();
			}

			Orders savedOrder = ordersRepository.save(orders);
			saveOrdersBooks(savedOrder, books);

			orderSagaMessage.setStatus("SUCCESS_SAVE_ORDERS_DATABASE");
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			orderSagaMessage.setStatus("FAIL_SAVE_ORDERS_DATABASE");
		} finally {
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "nova.api5-producer-routing-key", orderSagaMessage);
		}
	}

	private void saveOrdersBooks(Orders savedOrder, List<BookIdAndQuantityDTO> books) {
		for (BookIdAndQuantityDTO bookDto : books) {
			Book book = bookRepository.findById(bookDto.id())
				.orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_NOT_FOUND));
			OrdersBook ordersBook = new OrdersBook(savedOrder, book, (int) bookDto.quantity(), book.getPrice());
			ordersBookRepository.save(ordersBook);
		}
	}

	private OrderTemporaryNonMemberForm getOrderTemporaryNonMemberForm(UUID orderId) {
		return redisOrderNonMemberRepository.findById(orderId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.ORDERS_NOT_FOUND));
	}

	private OrderTemporaryForm getOrderTemporaryForm(Long memberId) {
		return redisOrderRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.ORDERS_NOT_FOUND));
	}

	private CreateOrdersRequest createOrdersRequestForNonMember(OrderSagaMessage orderSagaMessage, OrderTemporaryNonMemberForm orderForm) {
		return CreateOrdersRequest.builder()
			.memberId(null)
			.totalAmount(orderSagaMessage.getCalculateTotalAmount())
			.ordersDate(LocalDateTime.now())
			.deliveryAddress(orderForm.orderReceiverInfo().orderAddressInfo().streetAddress() +
				orderForm.orderReceiverInfo().orderAddressInfo().detailAddress())
			.deliveryDate(orderForm.deliveryDate().atTime(0, 0))
			.receiverName(orderForm.orderReceiverInfo().name())
			.receiverNumber(orderForm.orderReceiverInfo().phone())
			.bookPurchaseAmount(orderSagaMessage.getBookAmount())
			.couponDiscountAmount(orderSagaMessage.getCouponAmount())
			.pointSaveAmount(orderSagaMessage.getEarnPointAmount())
			.build();
	}

	private CreateOrdersRequest createOrdersRequestForMember(OrderSagaMessage orderSagaMessage, OrderTemporaryForm orderForm) {
		return CreateOrdersRequest.builder()
			.memberId(orderSagaMessage.getPaymentRequest().memberId())
			.totalAmount(orderSagaMessage.getCalculateTotalAmount())
			.ordersDate(LocalDateTime.now())
			.deliveryAddress(orderForm.orderReceiverInfo().orderAddressInfo().streetAddress() +
				orderForm.orderReceiverInfo().orderAddressInfo().detailAddress())
			.deliveryDate(orderForm.deliveryDate().atTime(0, 0))
			.receiverName(orderForm.orderReceiverInfo().name())
			.receiverNumber(orderForm.orderReceiverInfo().phone())
			.bookPurchaseAmount(orderSagaMessage.getBookAmount())
			.couponDiscountAmount(orderSagaMessage.getCouponAmount())
			.pointSaveAmount(orderSagaMessage.getEarnPointAmount())
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
