package store.novabook.store.orders.service.impl;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
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
		BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> tossOrderService.create(orderSagaMessage));

		// Then
		Assertions.assertEquals(ErrorCode.PAYMENT_AMOUNT_MISMATCH, exception.getErrorCode());
		Assertions.assertEquals("FAIL_APPROVE_PAYMENT", orderSagaMessage.getStatus());
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
		Assertions.assertEquals("SUCCESS_REFUND_PAYMENT", orderSagaMessage.getStatus());
	}

	@Test
	void testCancel_Failure() throws Exception {
		// Given
		OrderSagaMessage orderSagaMessage = createOrderSagaMessage(1000L, 1000L);
		orderSagaMessage.setStatus("FAIL_REFUND_TOSS_PAYMENT");

		doNothing().when(rabbitTemplate).convertAndSend(Optional.ofNullable(any()), any(), any());
		doThrow(new IOException()).when(spyTossOrderService).sendTossCancelRequest(any());

		// When
		Assertions.assertThrows(RuntimeException.class, () -> {
			spyTossOrderService.cancel(orderSagaMessage);
		});

		// Then
		Assertions.assertEquals("FAIL_REFUND_TOSS_PAYMENT", orderSagaMessage.getStatus());
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
		Assertions.assertEquals("testStatus", message.getStatus());
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
		Assertions.assertThrows(RuntimeException.class, () -> {
			spyTossOrderService.paymentRequestPayCancel(message);
		});

		// Then
		Assertions.assertEquals("FAIL_REQUEST_CANCEL_TOSS_PAYMENT", message.getStatus());
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
