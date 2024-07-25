package store.novabook.store.common.config;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

// @SpringBootTest
// class OrdersRabbitMQConfigTest {
//
// 	@Autowired
// 	private ApplicationContext applicationContext;
//
// 	@Test
// 	void testBeansLoaded() {
// 		// 리스트에 빈 이름 추가
// 		List<String> expectedBeans = List.of("sagaExchange", "converter", "ordersVerifyFormQueue",
// 			"ordersDecrementPointQueue", "ordersApplyCouponQueue", "ordersPaymentApproveQueue",
// 			"ordersSaveOrderDatabaseQueue", "pointEarnQueue", "cartDeleteQueue", "paymentCancelQueue",
// 			"pointRequestPayAmountQueue", "requestPayCancelQueue", "ordersRequestPayCancelQueue",
// 			"compensateOrdersConfirmFormQueue", "compensateOrdersDecrementPointQueue",
// 			"compensateOrdersApplyCouponQueue", "compensateApprovePaymentQueue", "deadOrdersSagaQueue",
// 			"api1ProducerQueue", "api2ProducerQueue", "api3ProducerQueue", "api4ProducerQueue", "api5ProducerQueue",
// 			"api6ProducerQueue", "ordersConfirmBinding", "decrementPointBinding", "applyCouponBinding",
// 			"approvePaymentBinding", "saveOrdersDatabaseBinding", "earnPointBinding", "deleteCartBinding",
// 			"cancelPaymentBinding", "pointRequestPayCancelBinding", "ordersRequestPayCancelBinding",
// 			"requestPayCancelBinding", "deadOrdersSagaBinding", "compensateOrdersConfirmBinding",
// 			"compensateDecrementPointBinding", "compensateApplyCouponBinding", "compensateApprovePaymentBinding",
// 			"api1ProducerBinding", "api2ProducerBinding", "api3ProducerBinding", "api4ProducerBinding",
// 			"api5ProducerBinding", "api6ProducerBinding", "ordersRabbitTemplate", "rabbitListenerContainerFactory");
//
// 		assertAll(expectedBeans.stream()
// 			.map(
// 				beanName -> () -> assertThat(applicationContext.containsBean(beanName)).as("Checking bean: " + beanName)
// 					.isTrue()));
// 	}
// }
