package store.novabook.store.common.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrdersRabbitMQConfig {

	@Bean
	public Exchange sagaExchange() {
		return ExchangeBuilder.directExchange("nova.orders.saga.exchange").build();
	}

	@Bean
	public MessageConverter converter() {
		return new Jackson2JsonMessageConverter();
	}

	// QUEUES
	@Bean
	public Queue ordersConfirmFormQueue() {
		return QueueBuilder.durable("Queue nova.orders.form.confirm.queue").build();
	}

	// BINDING
	@Bean
	public Binding ordersConfirmBinding() {
		return BindingBuilder.bind(ordersConfirmFormQueue()).to(sagaExchange())
			.with("orders.form.confirm").noargs();
	}
	@Bean
	public RabbitTemplate ordersRabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(converter());
		return rabbitTemplate;
	}

	@Bean
	public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(converter());
		return factory;
	}


	// private Map<String, Object> queueArguments(String queueName) {
	// 	Map<String, Object> args = new HashMap<>();
	// 	args.put("x-dead-letter-exchange", "nova.orders.deadletter.exchange");
	// 	args.put("x-dead-letter-routing-key", "orders.form.confirm");
	// 	args.put("x-original-queue", queueName);
	// 	args.put("x-queue-type", "classic");
	// 	return args;
	// }
}
