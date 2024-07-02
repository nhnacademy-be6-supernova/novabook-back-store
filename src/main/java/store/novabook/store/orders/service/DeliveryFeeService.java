package store.novabook.store.orders.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.orders.dto.request.CreateDeliveryFeeRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetDeliveryFeeResponse;

public interface DeliveryFeeService {
	CreateResponse createFee(CreateDeliveryFeeRequest request);
	@Transactional(readOnly = true)
	long latestDeliveryFee();

	@Transactional(readOnly = true)
	Page<GetDeliveryFeeResponse> findAllDeliveryFees(Pageable pageable);

	@Transactional(readOnly = true)
	List<GetDeliveryFeeResponse> findAllDeliveryFeeList();

	@Transactional(readOnly = true)
	GetDeliveryFeeResponse getDeliveryFee(Long id);
}
