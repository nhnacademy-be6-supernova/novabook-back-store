package store.novabook.store.member.service.impl;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import store.novabook.store.member.service.DoorayHookClient;
import store.novabook.store.orders.dto.OrderSagaMessage;
import store.novabook.store.orders.service.DeadOrdersDoorayHookClient;

@ExtendWith(MockitoExtension.class)
class DoorayServiceImplTest {

	@InjectMocks
	private DoorayServiceImpl doorayService;

	@Mock
	private DoorayHookClient doorayHookClient;

	@Mock
	private DeadOrdersDoorayHookClient deadOrdersDoorayHookClient;

	@BeforeEach
	void setUp() {
	}

	@Test
	void sendAuthCode() {
		String uuid = "test-uuid";
		String authCode = "123456";

		doorayService.sendAuthCode(uuid, authCode);

		Map<String, Object> expectedRequest = new HashMap<>();
		expectedRequest.put("botName", "novabook Bot");
		expectedRequest.put("text", "휴면 계정 해지를 위한 인증코드: " + authCode);

		verify(doorayHookClient, times(1)).sendMessage(eq(expectedRequest));
	}

	@Test
	void sendDeadMessage() {
		OrderSagaMessage orderSagaMessage = OrderSagaMessage.builder()
			.bookAmount(10000L)
			.calculateTotalAmount(12000L)
			.couponAmount(2000L)
			.earnPointAmount(500L)
			.noEarnPoint(false)
			.noUsePoint(false)
			.noUseCoupon(false)
			.status("ERROR")
			.build();

		doorayService.sendDeadMessage(orderSagaMessage);

		Map<String, Object> expectedRequest = new HashMap<>();
		expectedRequest.put("botName", "novabook Bot");
		expectedRequest.put("text", "주문 처리중 문제가 발생했습니다 \n상태:  " + orderSagaMessage.getStatus());

		verify(deadOrdersDoorayHookClient, times(1)).sendMessage(eq(expectedRequest));
	}

	@Test
	void testSendDeadMessage() {
		doorayService.sendDeadMessage();

		Map<String, Object> expectedRequest = new HashMap<>();
		expectedRequest.put("botName", "novabook Bot");
		expectedRequest.put("text", "쿠폰 처리중 문제가 발생했습니다");

		verify(deadOrdersDoorayHookClient, times(1)).sendMessage(eq(expectedRequest));
	}
}