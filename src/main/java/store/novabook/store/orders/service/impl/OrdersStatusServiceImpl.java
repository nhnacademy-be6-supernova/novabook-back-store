package store.novabook.store.orders.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.orders.dto.CreateOrdersStatusRequest;
import store.novabook.store.orders.dto.CreateResponse;
import store.novabook.store.orders.dto.GetOrdersStatusResponse;
import store.novabook.store.orders.dto.UpdateOrdersStatusRequest;
import store.novabook.store.orders.entity.OrdersStatus;
import store.novabook.store.orders.repository.OrdersStatusRepository;
import store.novabook.store.orders.service.OrdersStatusService;

@Service
@RequiredArgsConstructor
@Transactional
public class OrdersStatusServiceImpl implements OrdersStatusService {
	private final OrdersStatusRepository ordersStatusRepository;

	@Override
	public CreateResponse save(CreateOrdersStatusRequest request) {
		OrdersStatus ordersStatus = new OrdersStatus(request);
		ordersStatusRepository.save(ordersStatus);
		return new CreateResponse(ordersStatus.getId());
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetOrdersStatusResponse> getOrdersStatus() {
		List<OrdersStatus> ordersStatusList = ordersStatusRepository.findAll();
		List<GetOrdersStatusResponse> responses = new ArrayList<>();
		for (OrdersStatus ordersStatus : ordersStatusList) {
			responses.add(GetOrdersStatusResponse.form(ordersStatus));
		}
		return new PageImpl<>(responses);
	}

	@Override
	@Transactional(readOnly = true)
	public GetOrdersStatusResponse getOrdersStatus(Long id) {
		OrdersStatus ordersStatus = ordersStatusRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(OrdersStatus.class, id));
		return GetOrdersStatusResponse.form(ordersStatus);
	}

	@Override
	public void updateOrdersStatus(Long id, UpdateOrdersStatusRequest request) {
		OrdersStatus ordersStatus = ordersStatusRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(OrdersStatus.class, id));
		ordersStatus.update(request);
	}

}
