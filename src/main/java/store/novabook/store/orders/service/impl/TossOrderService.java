package store.novabook.store.orders.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import store.novabook.store.common.exception.BadRequestException;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.orders.dto.OrderSagaMessage;
import store.novabook.store.orders.dto.RequestPayCancelMessage;
import store.novabook.store.orders.dto.request.TossPaymentCancelRequest;

@RequiredArgsConstructor
@Service
@Slf4j
public class TossOrderService {

	public static final String NOVA_ORDERS_SAGA_EXCHANGE = "nova.orders.saga.exchange";
	public static final String TOSS_CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";
	private final RabbitTemplate rabbitTemplate;
	private static final String AMOUNT = "amount";
	private static final String PAYMENT_KEY = "paymentKey";
	private static final String WIDGET_SECRET_KEY = "test_sk_LkKEypNArWLkZabM1Rbz8lmeaxYG";



	@Transactional
	@RabbitListener(queues = "nova.orders.approve.payment.queue")
	public void create(@Payload OrderSagaMessage orderSagaMessage) {
		try {
			JSONParser parser = new JSONParser();
			JSONObject obj = new JSONObject();

			@SuppressWarnings("unchecked")
			HashMap<String, Object> paymentParam = (HashMap<String, Object>)orderSagaMessage.getPaymentRequest()
				.paymentInfo();

			Integer tossAmountInt = (Integer)paymentParam.get(AMOUNT);
			long tossAmount = tossAmountInt.longValue();

			if (tossAmount != orderSagaMessage.getCalculateTotalAmount()) {
				throw new BadRequestException(ErrorCode.PAYMENT_AMOUNT_MISMATCH);
			}

			obj.put("orderId", orderSagaMessage.getPaymentRequest().orderCode());
			obj.put(AMOUNT, paymentParam.get(AMOUNT));
			obj.put(PAYMENT_KEY, paymentParam.get(PAYMENT_KEY));

			// 토스페이먼츠 API는 시크릿 키를 사용자 ID로 사용하고, 비밀번호는 사용하지 않습니다.
			// 비밀번호가 없다는 것을 알리기 위해 시크릿 키 뒤에 콜론을 추가합니다.
			Base64.Encoder encoder = Base64.getEncoder();
			byte[] encodedBytes = encoder.encode((WIDGET_SECRET_KEY + ":").getBytes(StandardCharsets.UTF_8));
			String authorizations = "Basic " + new String(encodedBytes);

			URL url = new URL(TOSS_CONFIRM_URL);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestProperty("Authorization", authorizations);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);

			OutputStream outputStream = connection.getOutputStream();
			outputStream.write(obj.toString().getBytes(StandardCharsets.UTF_8));

			// 결제를 승인하면 결제수단에서 금액이 차감돼요.
			int code = connection.getResponseCode();
			boolean isSuccess = code == 200;

			InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

			Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
			JSONObject jsonObject = (JSONObject)parser.parse(reader);
			responseStream.close();

			if (isSuccess) {
				orderSagaMessage.setStatus("SUCCESS_APPROVE_PAYMENT");
			} else {
				orderSagaMessage.setStatus("FAIL_APPROVE_PAYMENT");
			}
			log.info("결제 응답 내용 : {}", jsonObject.toString());
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			orderSagaMessage.setStatus("FAIL_APPROVE_PAYMENT");
		} finally {
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "nova.api4-producer-routing-key",
				orderSagaMessage);
		}
	}

	@RabbitListener(queues = "nova.orders.compensate.approve.payment.queue")
	@Transactional
	public void cancel(@Payload OrderSagaMessage orderSagaMessage) {
		@SuppressWarnings("unchecked")
		HashMap<String, String> paymentParam = (HashMap<String, String>)orderSagaMessage.getPaymentRequest()
			.paymentInfo();
		String paymentKey = paymentParam.get(PAYMENT_KEY);

		try {
			TossPaymentCancelRequest tossPaymentCancel = TossPaymentCancelRequest.builder()
				.cancelReason("서버오류 결제 보상 트랜잭션")
				.paymentKey(paymentKey)
				.build();

			sendTossCancelRequest(tossPaymentCancel);
			orderSagaMessage.setStatus("SUCCESS_REFUND_PAYMENT");
		} catch (IOException | ParseException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			orderSagaMessage.setStatus("FAIL_REFUND_TOSS_PAYMENT");
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "nova.orders.saga.dead.routing.key",
				orderSagaMessage);
			throw new RuntimeException(e);
		}
	}


	public void sendTossCancelRequest(TossPaymentCancelRequest tossPaymentCancelRequest) throws
		IOException,
		ParseException {
		JSONParser parser = new JSONParser();
		JSONObject obj = new JSONObject();
		obj.put("cancelReason", tossPaymentCancelRequest.cancelReason());

		Base64.Encoder encoder = Base64.getEncoder();
		byte[] encodedBytes = encoder.encode((WIDGET_SECRET_KEY + ":").getBytes(StandardCharsets.UTF_8));
		String authorizations = "Basic " + new String(encodedBytes);

		URL url = new URL("https://api.tosspayments.com/v1/payments/" + tossPaymentCancelRequest.paymentKey() + "/cancel");
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestProperty("Authorization", authorizations);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);

		OutputStream outputStream = connection.getOutputStream();
		outputStream.write(obj.toString().getBytes(StandardCharsets.UTF_8));

		int code = connection.getResponseCode();
		boolean isSuccess = code == 200;

		InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();
		Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
		JSONObject jsonObject = (JSONObject)parser.parse(reader);
		responseStream.close();

		if (!isSuccess) {
			log.error("토스 환불 실패 {}", jsonObject.toString());
		}

		log.info("jsonObject");
	}

	@RabbitListener(queues = "nova.payment.request.pay.cancel.queue")
	public void paymentRequestPayCancel(@Payload RequestPayCancelMessage message) {
		try {
			sendTossCancelRequest(TossPaymentCancelRequest.builder().paymentKey(message.getPaymentKey())
				.cancelReason("결제 취소 요청").build());
		} catch (IOException | ParseException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			message.setStatus("FAIL_REQUEST_CANCEL_TOSS_PAYMENT");
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "nova.orders.saga.dead.routing.key",
				message);
		}
	}
}
