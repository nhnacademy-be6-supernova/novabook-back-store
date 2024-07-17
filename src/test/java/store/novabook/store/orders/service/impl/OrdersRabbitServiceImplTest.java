package store.novabook.store.orders.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.transaction.NoTransactionException;

import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.BookStatus;
import store.novabook.store.book.entity.BookStatusEnum;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.book.repository.BookStatusRepository;
import store.novabook.store.member.entity.MemberGradeHistory;
import store.novabook.store.member.entity.MemberGradePolicy;
import store.novabook.store.member.repository.MemberGradeHistoryRepository;
import store.novabook.store.member.repository.MemberRepository;
import store.novabook.store.orders.dto.OrderSagaMessage;
import store.novabook.store.orders.dto.RequestPayCancelMessage;
import store.novabook.store.orders.dto.request.BookIdAndQuantityDTO;
import store.novabook.store.orders.dto.request.CreateDeliveryFeeRequest;
import store.novabook.store.orders.dto.request.CreateOrdersBookRequest;
import store.novabook.store.orders.dto.request.CreateOrdersStatusRequest;
import store.novabook.store.orders.dto.request.CreateWrappingPaperRequest;
import store.novabook.store.orders.dto.request.OrderAddressInfo;
import store.novabook.store.orders.dto.request.OrderReceiverInfo;
import store.novabook.store.orders.dto.request.OrderSenderInfo;
import store.novabook.store.orders.dto.request.OrderTemporaryForm;
import store.novabook.store.orders.dto.request.OrderTemporaryNonMemberForm;
import store.novabook.store.orders.dto.request.PaymentRequest;
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
import store.novabook.store.payment.dto.request.CreatePaymentRequest;
import store.novabook.store.payment.entity.Payment;
import store.novabook.store.payment.repository.PaymentRepository;
import store.novabook.store.point.entity.PointPolicy;
import store.novabook.store.point.repository.PointPolicyRepository;

@ExtendWith(MockitoExtension.class)
class OrdersRabbitServiceImplTest {

	@Mock
	private OrdersRepository ordersRepository;
	@Mock
	private DeliveryFeeRepository deliveryFeeRepository;
	@Mock
	private WrappingPaperRepository wrappingPaperRepository;
	@Mock
	private OrdersStatusRepository ordersStatusRepository;
	@Mock
	private MemberRepository memberRepository;
	@Mock
	private BookRepository bookRepository;
	@Mock
	private BookStatusRepository bookStatusRepository;
	@Mock
	private OrdersBookRepository ordersBookRepository;
	@Mock
	private PointPolicyRepository pointPolicyRepository;
	@Mock
	private RedisOrderRepository redisOrderRepository;
	@Mock
	private RedisOrderNonMemberRepository redisOrderNonMemberRepository;
	@Mock
	private RabbitTemplate rabbitTemplate;
	@Mock
	private MemberGradeHistoryRepository memberGradeHistoryRepository;
	@Mock
	private PaymentRepository paymentRepository;

	@InjectMocks
	private OrdersRabbitServiceImpl ordersRabbitServiceImpl;

	private OrderSagaMessage orderSagaMessage;
	private OrderTemporaryNonMemberForm orderForm;

	private BookStatus bookStatus;

	@BeforeEach
	public void setUp() {
		setUpCommonOrderForm();
	}

	private void setUpCommonOrderForm() {

		Map<String, Object> paymentParam = new HashMap<>();
		paymentParam.put("paymentKey", "code");

		PaymentRequest testPaymentRequest = PaymentRequest.builder()
			.orderCode("TESTCODE1")
			.paymentInfo(paymentParam)
			.build();

		orderSagaMessage = OrderSagaMessage.builder()
			.earnPointAmount(1000L)
			.paymentRequest(testPaymentRequest)
			.bookAmount(1000L)
			.calculateTotalAmount(2000L)
			.couponAmount(1000L)
			.status("PROCEED_TEST")
			.build();

		BookIdAndQuantityDTO book1 = new BookIdAndQuantityDTO(1L, 2);
		BookIdAndQuantityDTO book2 = new BookIdAndQuantityDTO(2L, 1);

		OrderSenderInfo senderInfo = OrderSenderInfo.builder()
			.name("Sender Name")
			.phone("123-456-7890")
			.build();

		OrderReceiverInfo receiverInfo = OrderReceiverInfo.builder()
			.name("Receiver Name")
			.phone("098-765-4321")
			.orderAddressInfo(OrderAddressInfo.builder()
				.streetAddress("123 Main St")
				.detailAddress("Apt 4B")
				.build())
			.build();

		orderForm = OrderTemporaryNonMemberForm.builder()
			.orderCode("ORDER12345")
			.cartUUID("CART_UUID_67890")
			.books(List.of(book1, book2))
			.wrappingPaperId(1L)
			.couponId(2L)
			.usePointAmount(100)
			.deliveryDate(LocalDate.now().plusDays(3))
			.deliveryId(3L)
			.orderSenderInfo(senderInfo)
			.orderReceiverInfo(receiverInfo)
			.build();

		bookStatus = BookStatus.of(BookStatusEnum.FOR_SALE.getKoreanValue());
	}

	@Test
	@DisplayName("가주문서 비회원 테스트 - 성공 로직")
	void confirmOrderForm() {
		mockCommonRepositories();
		when(redisOrderNonMemberRepository.findById(any())).thenReturn(Optional.of(orderForm));

		ordersRabbitServiceImpl.confirmOrderForm(orderSagaMessage);

		verify(bookRepository, times(2)).findById(anyLong());
		verify(bookRepository, times(2)).save(any(Book.class));
		verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), any(OrderSagaMessage.class));
		assertEquals("SUCCESS_CONFIRM_ORDER_FORM", orderSagaMessage.getStatus());
	}

	@Test
	@DisplayName("가주문서 회원 테스트 - 성공 로직")
	void confirmOrderFormForMember() {
		PaymentRequest testPaymentRequest = PaymentRequest.builder()
			.orderCode("TESTCODE1")
			.memberId(1L)
			.build();

		orderSagaMessage = OrderSagaMessage.builder()
			.earnPointAmount(1000L)
			.paymentRequest(testPaymentRequest)
			.bookAmount(1000L)
			.calculateTotalAmount(2000L)
			.couponAmount(1000L)
			.status("PROCEED_TEST")
			.build();

		BookIdAndQuantityDTO book1 = new BookIdAndQuantityDTO(1L, 2);
		BookIdAndQuantityDTO book2 = new BookIdAndQuantityDTO(2L, 1);

		OrderSenderInfo senderInfo = OrderSenderInfo.builder()
			.name("Sender Name")
			.phone("123-456-7890")
			.build();

		OrderReceiverInfo receiverInfo = OrderReceiverInfo.builder()
			.name("Receiver Name")
			.phone("098-765-4321")
			.orderAddressInfo(OrderAddressInfo.builder()
				.streetAddress("123 Main St")
				.detailAddress("Apt 4B")
				.build())
			.build();

		OrderTemporaryForm orderTemporaryForm = OrderTemporaryForm.builder()
			.memberId(1L)
			.orderCode("CODE")
			.books(List.of(book1, book2))
			.deliveryDate(LocalDate.now())
			.couponId(1L)
			.orderReceiverInfo(receiverInfo)
			.orderSenderInfo(senderInfo)
			.build();

		bookStatus = BookStatus.of(BookStatusEnum.FOR_SALE.getKoreanValue());

		when(pointPolicyRepository.findTopByOrderByCreatedAtDesc()).thenReturn(
			Optional.ofNullable(PointPolicy.builder().basicPointRate(1).reviewPoint(1).build()));

		when(memberGradeHistoryRepository.findFirstByMemberIdOrderByCreatedAtDesc(anyLong())).thenReturn(
			Optional.ofNullable(MemberGradeHistory.builder().memberGradePolicy(MemberGradePolicy.builder()
				.maxRange(1L)
				.saveRate(1L)
				.minRange(1L)
				.build()).quarter(LocalDate.now().atStartOfDay()).build()));

		mockCommonRepositories();
		when(redisOrderRepository.findById(any())).thenReturn(Optional.of(orderTemporaryForm));

		ordersRabbitServiceImpl.confirmOrderForm(orderSagaMessage);

		verify(bookRepository, times(2)).findById(anyLong());
		verify(bookRepository, times(2)).save(any(Book.class));
		verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), any(OrderSagaMessage.class));
		assertEquals("SUCCESS_CONFIRM_ORDER_FORM", orderSagaMessage.getStatus());
	}

	private void mockCommonRepositories() {
		when(wrappingPaperRepository.findById(any())).thenReturn(Optional.of(WrappingPaper.builder()
			.request(CreateWrappingPaperRequest.builder().price(1000L).build()).build()));

		when(deliveryFeeRepository.findById(any())).thenReturn(Optional.of(DeliveryFee.builder()
			.createDeliveryFeeRequest(CreateDeliveryFeeRequest.builder().fee(1000L).build()).build()));

		when(bookRepository.findById(any())).thenReturn(Optional.of(Book.builder()
			.inventory(10).price(5000L).discountPrice(4000L)
			.bookStatus(bookStatus).build()));
	}

	@Test
	@DisplayName("보상 트랜잭션 재고 증가 로직 테스트 - 성공")
	void compensateConfirmOrderForm() {
		when(bookRepository.findById(any())).thenReturn(Optional.of(Book.builder()
			.inventory(10).price(5000L).discountPrice(4000L)
			.bookStatus(bookStatus).build()));

		when(redisOrderNonMemberRepository.findById(any())).thenReturn(Optional.of(orderForm));

		ordersRabbitServiceImpl.compensateConfirmOrderForm(orderSagaMessage);

		verify(bookRepository, times(1)).findById(anyLong());
	}

	@Test
	@DisplayName("주문 정보를 저장하는 테스트 코드 - 성공")
	void saveSagaOrder() {
		mockCommonRepositories();
		when(bookRepository.findById(anyLong())).thenReturn(Optional.of(Book.builder()
			.inventory(10).price(5000L).discountPrice(4000L)
			.bookStatus(bookStatus).build()));
		when(paymentRepository.save(any(Payment.class))).thenReturn(Payment.builder().request(
			CreatePaymentRequest.builder().build()).build());
		when(ordersRepository.save(any(Orders.class))).thenAnswer(invocation -> invocation.getArgument(0));
		when(redisOrderNonMemberRepository.findById(any())).thenReturn(Optional.of(orderForm));

		when(ordersStatusRepository.findById(any())).thenReturn(Optional.ofNullable(OrdersStatus.builder().request(
			CreateOrdersStatusRequest.builder().name("대기").build()).build()));

		ordersRabbitServiceImpl.saveSagaOrder(orderSagaMessage);

		verify(ordersRepository, times(1)).save(any(Orders.class));
		verify(paymentRepository, times(1)).save(any(Payment.class));
		verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), any(OrderSagaMessage.class));
		assertEquals("SUCCESS_SAVE_ORDERS_DATABASE", orderSagaMessage.getStatus());
	}

	@Test
	@DisplayName("주문 정보를 저장하는 테스트 코드 - 실패")
	void saveSagaOrder_Fail() {
		when(paymentRepository.save(any(Payment.class))).thenThrow(new NoTransactionException("Payment save failed"));

		RuntimeException exception = assertThrows(NoTransactionException.class, () -> {
			ordersRabbitServiceImpl.saveSagaOrder(orderSagaMessage);
		});

		assertEquals("No transaction aspect-managed TransactionStatus in scope", exception.getMessage());

		verify(paymentRepository, times(1)).save(any(Payment.class));
		verify(ordersRepository, times(0)).save(any(Orders.class));
		verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), any(OrderSagaMessage.class));
		assertEquals("PROCEED_TEST", orderSagaMessage.getStatus());
	}

	@Test
	@DisplayName("주문 취소 테스트 - 성공 로직")
	void orderCancel() {
		Book book = spy(Book.builder().inventory(5).build());
		doReturn(1L).when(book).getId();

		OrdersBook ordersBook = spy(OrdersBook.builder()
			.book(book)
			.request(CreateOrdersBookRequest.builder().quantity(10).price(1000L).build())
			.build());

		List<OrdersBook> ordersBooks = List.of(ordersBook);

		when(ordersBookRepository.findByOrdersCode(anyString())).thenReturn(ordersBooks);
		when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

		RequestPayCancelMessage cancelMessage = RequestPayCancelMessage.builder()
			.orderCode("ORDER12345")
			.build();

		ordersRabbitServiceImpl.orderCancel(cancelMessage);

		verify(bookRepository, times(1)).findById(anyLong());
		verify(bookRepository, times(1)).save(any(Book.class));
	}

	@Test
	@DisplayName("주문 취소 테스트 - 실패 로직")
	void orderCancel_Fail() {
		when(ordersBookRepository.findByOrdersCode(anyString())).thenReturn(List.of());

		RequestPayCancelMessage cancelMessage = RequestPayCancelMessage.builder()
			.orderCode("ORDER12345")
			.build();

		ordersRabbitServiceImpl.orderCancel(cancelMessage);

		verify(bookRepository, times(0)).findById(anyLong());
		verify(bookRepository, times(0)).save(any(Book.class));
	}

}
