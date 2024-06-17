package store.novabook.store.point.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.store.point.repository.PointHistoryRepository;
import store.novabook.store.point.repository.PointPolicyRepository;

@Service
@RequiredArgsConstructor
public class PointService {
	private final PointHistoryRepository pointHistoryRepository;
	private final PointPolicyRepository pointPolicyRepository;
}
