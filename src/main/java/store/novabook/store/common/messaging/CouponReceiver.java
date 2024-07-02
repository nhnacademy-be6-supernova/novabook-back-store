package store.novabook.store.common.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.messaging.dto.RegisterCouponMessage;
import store.novabook.store.member.service.MemberCouponService;

@Service
@RequiredArgsConstructor
public class CouponReceiver {

	private final MemberCouponService memberCouponService;

	@RabbitListener(queues = "${rabbitmq.queue.couponRegisterHighTraffic}")
	public void receiveRegisterCouponMessage(RegisterCouponMessage message) {
		memberCouponService.createMemberCouponByMessage(message);
	}

}
