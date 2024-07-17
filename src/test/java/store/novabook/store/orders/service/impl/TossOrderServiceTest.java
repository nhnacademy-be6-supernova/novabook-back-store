package store.novabook.store.orders.service.impl;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import store.novabook.store.common.exception.BadRequestException;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.orders.dto.OrderSagaMessage;
import store.novabook.store.orders.dto.RequestPayCancelMessage;
import store.novabook.store.orders.dto.request.PaymentRequest;
import store.novabook.store.orders.dto.request.TossPaymentCancelRequest;

class TossOrderServiceTest {

	@Mock
	private RabbitTemplate rabbitTemplate;

	@InjectMocks
	private TossOrderService tossOrderService;

	private TossOrderService spyTossOrderService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		spyTossOrderService = spy(tossOrderService);
	}


	// Additional utility classes for mocking
	private static class ByteArrayOutputStreamMock extends java.io.ByteArrayOutputStream {
		@Override
		public void write(byte[] b) throws IOException {
			// No-op
		}
	}

	@Test
	void testCreate_PaymentAmountMismatch() throws Exception {
		// Given
		OrderSagaMessage orderSagaMessage = createOrderSagaMessage(1000, 2000);
		orderSagaMessage.setCalculateTotalAmount(2000);
		Map<String, Object> param = new HashMap<>();
		param.put("amount", 2000);

		orderSagaMessage.setPaymentRequest(PaymentRequest.builder().paymentInfo(param).build());

		doNothing().when(rabbitTemplate).convertAndSend(Optional.ofNullable(any()), any(), any());

		// When
		try {
			tossOrderService.create(orderSagaMessage);
		} catch (BadRequestException e) {
			assert e.getErrorCode() == ErrorCode.PAYMENT_AMOUNT_MISMATCH;
		}

		// Then
		assert orderSagaMessage.getStatus().equals("FAIL_APPROVE_PAYMENT");
	}

	@Test
	void testCancel_Success() throws Exception {
		// Given
		OrderSagaMessage orderSagaMessage = createOrderSagaMessage(1000L, 1000L);

		doNothing().when(rabbitTemplate).convertAndSend(any(String.class), any(String.class), any(Object.class));
		doNothing().when(spyTossOrderService).sendTossCancelRequest(any(TossPaymentCancelRequest.class));

		// When
		spyTossOrderService.cancel(orderSagaMessage);

		// Then
		assert orderSagaMessage.getStatus().equals("SUCCESS_REFUND_PAYMENT");
	}

	@Test
	void testCancel_Failure() throws Exception {
		// Given
		OrderSagaMessage orderSagaMessage = createOrderSagaMessage(1000L, 1000L);
		orderSagaMessage.setStatus("FAIL_REFUND_TOSS_PAYMENT");

		doNothing().when(rabbitTemplate).convertAndSend(Optional.ofNullable(any()), any(), any());
		doThrow(new IOException()).when(spyTossOrderService).sendTossCancelRequest(any());

		// When
		try {
			spyTossOrderService.cancel(orderSagaMessage);
		} catch (RuntimeException e) {
			// Expected exception
		}

		// Then
		assert orderSagaMessage.getStatus().equals("FAIL_REFUND_TOSS_PAYMENT");
	}

	@Test
	void testPaymentRequestPayCancel_Success() throws Exception {
		// Given
		String paymentKey = "paymentKey";
		RequestPayCancelMessage message = RequestPayCancelMessage.builder().paymentKey("testKey").status("testStatus")
			.status("testStatus")
			.earnPointAmount(1000L)
			.orderCode("orderCode").build();

		message.setPaymentKey(paymentKey);

		doNothing().when(rabbitTemplate).convertAndSend(any(String.class), any(String.class), any(Object.class));
		doNothing().when(spyTossOrderService).sendTossCancelRequest(any(TossPaymentCancelRequest.class));

		// When
		spyTossOrderService.paymentRequestPayCancel(message);

		// Then
		assert message.getStatus().equals("testStatus");
	}

	@Test
	void testPaymentRequestPayCancel_Failure() throws Exception {
		// Given
		RequestPayCancelMessage message = RequestPayCancelMessage.builder().paymentKey("testKey")
			.status("FAIL_REQUEST_CANCEL_TOSS_PAYMENT")
			.earnPointAmount(1000L)
			.orderCode("orderCode").build();

		doNothing().when(rabbitTemplate).convertAndSend(Optional.ofNullable(any()), any(), any());
		doThrow(new IOException()).when(spyTossOrderService).sendTossCancelRequest(any());

		// When
		try {
			spyTossOrderService.paymentRequestPayCancel(message);
		} catch (RuntimeException e) {
			// Expected exception
		}

		// Then
		assert message.getStatus().equals("FAIL_REQUEST_CANCEL_TOSS_PAYMENT");
	}

	private OrderSagaMessage createOrderSagaMessage(long calculateTotalAmount, long amount) {
		HashMap<String, Object> paymentInfo = new HashMap<>();
		paymentInfo.put("amount", amount);
		paymentInfo.put("paymentKey", "testPaymentKey");

		PaymentRequest paymentRequest = PaymentRequest.builder()
			.orderCode("orderCode")
			.paymentInfo(paymentInfo)
			.build();

		return OrderSagaMessage.builder()
			.calculateTotalAmount(calculateTotalAmount)
			.paymentRequest(paymentRequest)
			.build();
	}
}




