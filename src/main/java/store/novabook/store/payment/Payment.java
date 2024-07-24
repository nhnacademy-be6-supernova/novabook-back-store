package store.novabook.store.payment;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.simple.parser.ParseException;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.orders.dto.OrderSagaMessage;
import store.novabook.store.orders.dto.RequestPayCancelMessage;

public interface Payment {
	@Transactional
	void createOrder(@Payload OrderSagaMessage orderSagaMessage) throws
		URISyntaxException, IOException, ParseException;

	@Transactional
	void compensateCancelOrder(@Payload OrderSagaMessage orderSagaMessage) throws
		IOException,
		ParseException,
		URISyntaxException;

	void cancelOrder(@Payload RequestPayCancelMessage message) throws IOException, ParseException, URISyntaxException;
}
