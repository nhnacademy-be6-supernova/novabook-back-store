package store.novabook.store.member.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import jakarta.validation.Valid;
import store.novabook.store.member.dto.request.CreateMemberCouponRequest;
import store.novabook.store.member.dto.response.CreateMemberCouponResponse;
import store.novabook.store.message.CouponCreatedMessage;

public interface MemberCouponService {
	CreateMemberCouponResponse createMemberCoupon(Long memberId, CreateMemberCouponRequest request);

	@RabbitListener(queues = "${rabbitmq.queue.member-coupon}")
	void handleCouponCreatedMessage(@Valid CouponCreatedMessage message);
}
