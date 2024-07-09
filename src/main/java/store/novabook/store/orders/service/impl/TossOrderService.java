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
import store.novabook.store.orders.dto.OrderSagaMessage;

@RequiredArgsConstructor
@Service
@Slf4j
public class TossOrderService {

	public static final String NOVA_ORDERS_SAGA_EXCHANGE = "nova.orders.saga.exchange";
	private final RabbitTemplate rabbitTemplate;

	@Transactional
	@RabbitListener(queues = "nova.orders.approve.payment.queue")
	public void create(@Payload OrderSagaMessage orderSagaMessage) {

		JSONParser parser = new JSONParser();
		JSONObject obj = new JSONObject();

		HashMap<String, String> paymentParam = (HashMap<String, String>)orderSagaMessage.getPaymentRequest().paymentInfo();

		obj.put("orderId", orderSagaMessage.getPaymentRequest().orderId().toString());
		obj.put("amount", paymentParam.get("amount"));
		obj.put("paymentKey", paymentParam.get("paymentKey"));

		log.info("[TossPayment] 전달 받은 파라미터 값 : {}, {} , {}",
			orderSagaMessage.getPaymentRequest().orderId()
			, paymentParam.get("amount")
			, paymentParam.get("paymentKey")
		);

		// 토스페이먼츠 API는 시크릿 키를 사용자 ID로 사용하고, 비밀번호는 사용하지 않습니다.
		// 비밀번호가 없다는 것을 알리기 위해 시크릿 키 뒤에 콜론을 추가합니다.
		String widgetSecretKey = "test_sk_LkKEypNArWLkZabM1Rbz8lmeaxYG";
		Base64.Encoder encoder = Base64.getEncoder();
		byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
		String authorizations = "Basic " + new String(encodedBytes);

		try {
			URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
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

			// 결제 성공 및 실패 비즈니스 로직을 구현하세요.
			Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
			JSONObject jsonObject = (JSONObject)parser.parse(reader);
			responseStream.close();

			if(isSuccess) {
				orderSagaMessage.setStatus("SUCCESS_APPROVE_PAYMENT");
			} else {
				orderSagaMessage.setStatus("FAIL_APPROVE_PAYMENT");
			}
			log.info("결제 응답 내용 : {}" , jsonObject.toString());
		} catch (IOException | ParseException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			orderSagaMessage.setStatus("FAIL_APPROVE_PAYMENT");
			throw new RuntimeException(e);
		} finally {
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "nova.api4-producer-routing-key", orderSagaMessage);
		}
	}



	// 환불 처리

	@RabbitListener(queues = "nova.orders.compensate.approve.payment.queue")
	@Transactional
	public void cancel(@Payload OrderSagaMessage orderSagaMessage) {

	}
}
