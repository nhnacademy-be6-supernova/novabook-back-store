package store.novabook.store.orders.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.orders.dto.request.UpdateOrdersAdminRequest;
import store.novabook.store.orders.dto.response.GetOrdersAdminResponse;
import store.novabook.store.orders.dto.response.GetOrdersResponse;
import store.novabook.store.orders.entity.Orders;
import store.novabook.store.orders.entity.OrdersStatus;
import store.novabook.store.orders.repository.OrdersRepository;
import store.novabook.store.orders.repository.OrdersStatusRepository;
import store.novabook.store.orders.service.OrdersService;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrdersServiceImpl implements OrdersService {

	private final OrdersRepository ordersRepository;
	private final OrdersStatusRepository ordersStatusRepository;

	@Override
	public Page<GetOrdersAdminResponse> getOrdersAdminResponsesAll(Pageable pageable) {
		Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		Page<Orders> orders = ordersRepository.findAll(pageable);
		return orders.map(GetOrdersAdminResponse::from);
	}

	@Override
	public void update(Long id, UpdateOrdersAdminRequest request) {
		OrdersStatus ordersStatus = ordersStatusRepository.findById(request.ordersStatusId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.ORDERS_STATUS_NOT_FOUND));
		Orders orders = ordersRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorCode.ORDERS_NOT_FOUND));
		orders.updateStatus(ordersStatus);
	}

	@Override
	public GetOrdersResponse getOrdersById(Long id) {
		Orders orders = ordersRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorCode.ORDERS_NOT_FOUND));
		return GetOrdersResponse.form(orders);
	}
}
