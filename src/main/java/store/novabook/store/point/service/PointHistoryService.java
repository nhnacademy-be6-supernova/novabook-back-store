package store.novabook.store.point.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.exception.EntityNotFoundException;
import store.novabook.store.orders.entity.Orders;
import store.novabook.store.orders.repository.OrdersRepository;
import store.novabook.store.point.dto.CreatePointHistoryRequest;
import store.novabook.store.point.dto.GetPointHistoryResponse;
import store.novabook.store.point.entity.PointHistory;
import store.novabook.store.point.entity.PointPolicy;
import store.novabook.store.point.repository.PointHistoryRepository;
import store.novabook.store.point.repository.PointPolicyRepository;
import store.novabook.store.user.member.entity.Member;
import store.novabook.store.user.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class PointHistoryService {

	private final MemberRepository memberRepository;

	private final OrdersRepository ordersRepository;

	private final PointHistoryRepository pointHistoryRepository;

	private final PointPolicyRepository pointPolicyRepository;

	@Transactional(readOnly = true)
	public Page<GetPointHistoryResponse> getPointHistoryList(Pageable pageable) {
		Page<PointHistory> pointHistoryList = pointHistoryRepository.findAll(pageable);
		if (pointHistoryList.isEmpty()) {
			throw new EntityNotFoundException(PointHistory.class);
		}
		return pointHistoryList.map(pointHistory -> new GetPointHistoryResponse(
			pointHistory.getPointContent(),
			pointHistory.getPointAmount()
		));

	}

	public void createPointHistory(CreatePointHistoryRequest createPointHistoryRequest) {
		Orders orders = ordersRepository.findById(createPointHistoryRequest.ordersId())
			.orElseThrow(() -> new EntityNotFoundException(Orders.class, createPointHistoryRequest.ordersId()));

		Member member = memberRepository.findById(createPointHistoryRequest.memberId())
			.orElseThrow(() -> new EntityNotFoundException(Member.class, createPointHistoryRequest.memberId()));

		PointPolicy pointPolicy = pointPolicyRepository.findById(createPointHistoryRequest.pointPolicyId())
			.orElseThrow(
				() -> new EntityNotFoundException(PointPolicy.class, createPointHistoryRequest.pointPolicyId()));

		PointHistory pointHistory = new PointHistory(null,
			orders,
			pointPolicy,
			member,
			createPointHistoryRequest.pointContent(),
			createPointHistoryRequest.pointAmount(),
			LocalDateTime.now(),
			null);

		pointHistoryRepository.save(pointHistory);
	}
}
