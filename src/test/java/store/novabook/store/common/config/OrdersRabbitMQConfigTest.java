package store.novabook.store.common.config;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class OrdersRabbitMQConfigTest {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void testBeansLoaded() {
		// Exchange Bean
		assertThat(applicationContext.containsBean("sagaExchange")).isTrue();

		// MessageConverter Bean
		assertThat(applicationContext.containsBean("converter")).isTrue();

		// Queue Beans
		assertThat(applicationContext.containsBean("ordersVerifyFormQueue")).isTrue();
		assertThat(applicationContext.containsBean("ordersDecrementPointQueue")).isTrue();
		assertThat(applicationContext.containsBean("ordersApplyCouponQueue")).isTrue();
		assertThat(applicationContext.containsBean("ordersPaymentApproveQueue")).isTrue();
		assertThat(applicationContext.containsBean("ordersSaveOrderDatabaseQueue")).isTrue();
		assertThat(applicationContext.containsBean("pointEarnQueue")).isTrue();
		assertThat(applicationContext.containsBean("cartDeleteQueue")).isTrue();
		assertThat(applicationContext.containsBean("paymentCancelQueue")).isTrue();
		assertThat(applicationContext.containsBean("pointRequestPayAmountQueue")).isTrue();
		assertThat(applicationContext.containsBean("requestPayCancelQueue")).isTrue();
		assertThat(applicationContext.containsBean("ordersRequestPayCancelQueue")).isTrue();
		assertThat(applicationContext.containsBean("compensateOrdersConfirmFormQueue")).isTrue();
		assertThat(applicationContext.containsBean("compensateOrdersDecrementPointQueue")).isTrue();
		assertThat(applicationContext.containsBean("compensateOrdersApplyCouponQueue")).isTrue();
		assertThat(applicationContext.containsBean("compensateApprovePaymentQueue")).isTrue();
		assertThat(applicationContext.containsBean("deadOrdersSagaQueue")).isTrue();
		assertThat(applicationContext.containsBean("api1ProducerQueue")).isTrue();
		assertThat(applicationContext.containsBean("api2ProducerQueue")).isTrue();
		assertThat(applicationContext.containsBean("api3ProducerQueue")).isTrue();
		assertThat(applicationContext.containsBean("api4ProducerQueue")).isTrue();
		assertThat(applicationContext.containsBean("api5ProducerQueue")).isTrue();
		assertThat(applicationContext.containsBean("api6ProducerQueue")).isTrue();

		// Binding Beans
		assertThat(applicationContext.containsBean("ordersConfirmBinding")).isTrue();
		assertThat(applicationContext.containsBean("decrementPointBinding")).isTrue();
		assertThat(applicationContext.containsBean("applyCouponBinding")).isTrue();
		assertThat(applicationContext.containsBean("approvePaymentBinding")).isTrue();
		assertThat(applicationContext.containsBean("saveOrdersDatabaseBinding")).isTrue();
		assertThat(applicationContext.containsBean("earnPointBinding")).isTrue();
		assertThat(applicationContext.containsBean("deleteCartBinding")).isTrue();
		assertThat(applicationContext.containsBean("cancelPaymentBinding")).isTrue();
		assertThat(applicationContext.containsBean("pointRequestPayCancelBinding")).isTrue();
		assertThat(applicationContext.containsBean("ordersRequestPayCancelBinding")).isTrue();
		assertThat(applicationContext.containsBean("requestPayCancelBinding")).isTrue();
		assertThat(applicationContext.containsBean("deadOrdersSagaBinding")).isTrue();
		assertThat(applicationContext.containsBean("compensateOrdersConfirmBinding")).isTrue();
		assertThat(applicationContext.containsBean("compensateDecrementPointBinding")).isTrue();
		assertThat(applicationContext.containsBean("compensateApplyCouponBinding")).isTrue();
		assertThat(applicationContext.containsBean("compensateApprovePaymentBinding")).isTrue();
		assertThat(applicationContext.containsBean("api1ProducerBinding")).isTrue();
		assertThat(applicationContext.containsBean("api2ProducerBinding")).isTrue();
		assertThat(applicationContext.containsBean("api3ProducerBinding")).isTrue();
		assertThat(applicationContext.containsBean("api4ProducerBinding")).isTrue();
		assertThat(applicationContext.containsBean("api5ProducerBinding")).isTrue();
		assertThat(applicationContext.containsBean("api6ProducerBinding")).isTrue();

		// RabbitTemplate and SimpleRabbitListenerContainerFactory Beans
		assertThat(applicationContext.containsBean("ordersRabbitTemplate")).isTrue();
		assertThat(applicationContext.containsBean("rabbitListenerContainerFactory")).isTrue();
	}
}
