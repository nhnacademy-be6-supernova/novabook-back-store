package store.novabook.store.payment;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import store.novabook.store.common.exception.BadRequestException;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.orders.dto.OrderSagaMessage;
import store.novabook.store.orders.dto.RequestPayCancelMessage;
import store.novabook.store.orders.dto.request.TossPaymentCancelRequest;

@Slf4j
public class TossPayment implements Payment {
	private static final String TOSS_CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";
	private static final String WIDGET_SECRET_KEY = "test_sk_LkKEypNArWLkZabM1Rbz8lmeaxYG";
	private static final String AMOUNT = "amount";
	private static final String PAYMENT_KEY = "paymentKey";
	private static final String CANCEL_REASON = "cancelReason";

	private static String getAuthorizationHeader() {
		String credentials = WIDGET_SECRET_KEY + ":";
		return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
	}

	private static HttpURLConnection createConnection(String url, String authorization) throws IOException, URISyntaxException {
		URL uri = new URI(url).toURL();
		HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
		connection.setRequestProperty("Authorization", authorization);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		return connection;
	}

	private static void sendRequest(HttpURLConnection connection, JSONObject jsonObject) throws IOException {
		try (OutputStream outputStream = connection.getOutputStream()) {
			outputStream.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
		}
	}

	private static JSONObject getResponse(HttpURLConnection connection) throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		int responseCode = connection.getResponseCode();
		boolean isSuccess = responseCode == 200;

		try (InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();
			 Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {
			return (JSONObject) parser.parse(reader);
		}
	}

	@Override
	@Transactional
	public void createOrder(@Payload OrderSagaMessage orderSagaMessage) throws URISyntaxException, IOException, ParseException {
		@SuppressWarnings("unchecked")
		HashMap<String, Object> paymentParam = (HashMap<String, Object>) orderSagaMessage.getPaymentRequest().paymentInfo();
		Integer tossAmountInt = (Integer) paymentParam.get(AMOUNT);
		long tossAmount = tossAmountInt.longValue();

		if (tossAmount != orderSagaMessage.getCalculateTotalAmount()) {
			throw new BadRequestException(ErrorCode.PAYMENT_AMOUNT_MISMATCH);
		}

		JSONObject obj = new JSONObject();
		obj.put("orderId", orderSagaMessage.getPaymentRequest().orderCode());
		obj.put(AMOUNT, tossAmountInt);
		obj.put(PAYMENT_KEY, paymentParam.get(PAYMENT_KEY));

		String authorization = getAuthorizationHeader();
		HttpURLConnection connection = createConnection(TOSS_CONFIRM_URL, authorization);
		sendRequest(connection, obj);

		JSONObject response = getResponse(connection);
		if (connection.getResponseCode() == 200) {
			ResponseEntity.ok();
		} else {
			log.error("결제 응답 내용 : {}", response.toString());
			ResponseEntity.internalServerError();
		}
	}

	@Override
	@Transactional
	public void compensateCancelOrder(@Payload OrderSagaMessage orderSagaMessage) throws IOException, ParseException, URISyntaxException {
		@SuppressWarnings("unchecked")
		HashMap<String, String> paymentParam = (HashMap<String, String>) orderSagaMessage.getPaymentRequest().paymentInfo();
		String paymentKey = paymentParam.get(PAYMENT_KEY);

		TossPaymentCancelRequest tossPaymentCancel = TossPaymentCancelRequest.builder()
			.cancelReason("서버오류 결제 보상 트랜잭션")
			.paymentKey(paymentKey)
			.build();

		sendTossCancelRequest(tossPaymentCancel);
		orderSagaMessage.setStatus("SUCCESS_REFUND_PAYMENT");
	}

	public void sendTossCancelRequest(TossPaymentCancelRequest tossPaymentCancelRequest) throws IOException, ParseException, URISyntaxException {
		JSONObject obj = new JSONObject();
		obj.put(CANCEL_REASON, tossPaymentCancelRequest.cancelReason());

		String authorization = getAuthorizationHeader();
		String url = "https://api.tosspayments.com/v1/payments/" + tossPaymentCancelRequest.paymentKey() + "/cancel";
		HttpURLConnection connection = createConnection(url, authorization);
		sendRequest(connection, obj);

		JSONObject response = getResponse(connection);
		if (connection.getResponseCode() != 200) {
			log.error("토스 환불 실패 {}", response.toString());
		} else {
			log.info("환불 성공");
		}
	}

	@Override
	public void cancelOrder(@Payload RequestPayCancelMessage message) throws IOException, ParseException, URISyntaxException {
		TossPaymentCancelRequest tossPaymentCancelRequest = TossPaymentCancelRequest.builder()
			.paymentKey(message.getPaymentKey())
			.cancelReason("결제 취소 요청")
			.build();
		sendTossCancelRequest(tossPaymentCancelRequest);
	}
}
