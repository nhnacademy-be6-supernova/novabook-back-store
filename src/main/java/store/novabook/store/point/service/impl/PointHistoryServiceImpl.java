package store.novabook.store.point.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.repository.MemberRepository;
import store.novabook.store.orders.entity.Orders;
import store.novabook.store.orders.repository.OrdersRepository;
import store.novabook.store.point.dto.request.CreatePointHistoryRequest;
import store.novabook.store.point.dto.request.GetPointHistoryRequest;
import store.novabook.store.point.dto.response.GetPointHistoryListResponse;
import store.novabook.store.point.dto.response.GetPointHistoryResponse;
import store.novabook.store.point.entity.PointHistory;
import store.novabook.store.point.entity.PointPolicy;
import store.novabook.store.point.repository.PointHistoryRepository;
import store.novabook.store.point.repository.PointPolicyRepository;
import store.novabook.store.point.service.PointHistoryService;

@Service
@RequiredArgsConstructor
@Transactional
public class PointHistoryServiceImpl implements PointHistoryService {

	private final MemberRepository memberRepository;

	private final OrdersRepository ordersRepository;

	private final PointHistoryRepository pointHistoryRepository;

	private final PointPolicyRepository pointPolicyRepository;

	@Override
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

	@Override
	@Transactional(readOnly = true)
	public GetPointHistoryListResponse getPointHistory(GetPointHistoryRequest getPointHistoryRequest) {
		List<GetPointHistoryResponse> pointHistoryResponses = pointHistoryRepository.findByMemberId(
				getPointHistoryRequest.memberId())
			.stream()
			.map(pointHistory -> GetPointHistoryResponse.builder()
				.pointAmount(pointHistory.getPointAmount())
				.pointContent(pointHistory.getPointContent())
				.build())
			.toList();

		return GetPointHistoryListResponse.builder().pointHistoryResponseList(pointHistoryResponses).build();
	}


	@Override
	public void createPointHistory(CreatePointHistoryRequest createPointHistoryRequest) {
		Orders orders = ordersRepository.findById(createPointHistoryRequest.ordersId())
			.orElseThrow(() -> new EntityNotFoundException(Orders.class, createPointHistoryRequest.ordersId()));

		Member member = memberRepository.findById(createPointHistoryRequest.memberId())
			.orElseThrow(() -> new EntityNotFoundException(Member.class, createPointHistoryRequest.memberId()));

		PointPolicy pointPolicy = pointPolicyRepository.findById(createPointHistoryRequest.pointPolicyId())
			.orElseThrow(
				() -> new EntityNotFoundException(PointPolicy.class, createPointHistoryRequest.pointPolicyId()));

		PointHistory pointHistory = PointHistory.of( pointPolicy,  orders,  member,  createPointHistoryRequest.pointContent(),  createPointHistoryRequest.pointAmount());
		pointHistoryRepository.save(pointHistory);
	}
}
