package store.novabook.store.common.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrdersRabbitMQConfig {

	@Bean
	public Exchange sagaExchange() {
		return ExchangeBuilder.directExchange("nova.orders.saga.exchange").build();
	}

}
