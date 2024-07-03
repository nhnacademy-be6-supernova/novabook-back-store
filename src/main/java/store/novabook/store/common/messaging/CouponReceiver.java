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
	private final CouponNotifier couponNotifier;

	@RabbitListener(queues = "${rabbitmq.queue.couponRegisterHighTraffic}")
	public void receiveRegisterHighTrafficCouponMessage(RegisterCouponMessage message) {
		memberCouponService.createMemberCouponByMessage(message);
		couponNotifier.notify(String.valueOf(message.memberId()), "쿠폰이 성공적으로 발급되었습니다.");
	}

	@RabbitListener(queues = "${rabbitmq.queue.couponRegisterNormal}")
	public void receiveRegisterNormalCouponMessage(RegisterCouponMessage message) {
		memberCouponService.createMemberCouponByMessage(message);
		couponNotifier.notify(String.valueOf(message.memberId()), "쿠폰이 성공적으로 발급되었습니다.");
	}

}
