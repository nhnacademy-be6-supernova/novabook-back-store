package store.novabook.store.order.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.order.dto.CreateDeliveryFeeRequest;
import store.novabook.store.order.dto.CreateResponse;
import store.novabook.store.order.dto.GetDeliveryFeeResponse;
import store.novabook.store.order.entity.DeliveryFee;
import store.novabook.store.order.repository.DeliveryFeeRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryFeeService {
	private final DeliveryFeeRepository deliveryFeeRepository;

	//createOrders
	public CreateResponse createFee(CreateDeliveryFeeRequest request) {
		DeliveryFee deliveryFee = new DeliveryFee(request);
		deliveryFeeRepository.save(deliveryFee);
		return new CreateResponse(deliveryFee.getId());
	}

	//가장 최근 가격
	@Transactional(readOnly = true)
	public long latestDeliveryFee() {
		return deliveryFeeRepository.findTopFeeByOrderByIdDesc();
	}

	//전체 조회
	@Transactional(readOnly = true)
	public Page<GetDeliveryFeeResponse> findAllDeliveryFees() {
		List<DeliveryFee> deliveryFees = deliveryFeeRepository.findAll();
		List<GetDeliveryFeeResponse> responses = new ArrayList<>();
		for (DeliveryFee deliveryFee : deliveryFees) {
			responses.add(GetDeliveryFeeResponse.from(deliveryFee));
		}
		return new PageImpl<>(responses);
	}

	//단건 조회
	@Transactional(readOnly = true)
	public GetDeliveryFeeResponse getDeliveryFee(Long id) {
		return GetDeliveryFeeResponse.from(
			deliveryFeeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(DeliveryFee.class, id)));
	}
}
