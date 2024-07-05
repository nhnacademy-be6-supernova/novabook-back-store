package store.novabook.store.orders.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import store.novabook.store.orders.dto.request.CreateDeliveryFeeRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetDeliveryFeeResponse;

public interface DeliveryFeeService {
	CreateResponse createFee(CreateDeliveryFeeRequest request);
	Page<GetDeliveryFeeResponse> findAllDeliveryFees(Pageable pageable);

	List<GetDeliveryFeeResponse> findAllDeliveryFeeList();

	GetDeliveryFeeResponse getDeliveryFee(Long id);

	GetDeliveryFeeResponse getRecentDeliveryFee();
}
