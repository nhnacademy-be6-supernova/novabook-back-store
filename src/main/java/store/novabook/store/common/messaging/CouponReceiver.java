package store.novabook.store.common.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.messaging.dto.RegisterCouponMessage;
import store.novabook.store.member.service.MemberCouponService;

@Service
@RequiredArgsConstructor
public class CouponReceiver {

	public static final String SUCCESS_MESSAGE = "쿠폰이 성공적으로 발급되었습니다.";
	private final MemberCouponService memberCouponService;

	@RabbitListener(queues = "${rabbitmq.queue.couponRegisterNormal}")
	public void receiveRegisterNormalCouponMessage(RegisterCouponMessage message) {
		memberCouponService.createMemberCouponByMessage(message);
	}

}
