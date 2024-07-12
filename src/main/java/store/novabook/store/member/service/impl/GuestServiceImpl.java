package store.novabook.store.member.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.member.service.GuestService;
import store.novabook.store.orders.dto.request.GetGuestOrderHistoryRequest;
import store.novabook.store.orders.dto.response.GetOrdersResponse;
import store.novabook.store.orders.entity.Orders;
import store.novabook.store.orders.repository.OrdersBookRepository;
import store.novabook.store.orders.repository.OrdersRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class GuestServiceImpl implements GuestService {
	private final OrdersBookRepository ordersBookRepository;
	private final OrdersRepository ordersRepository;

	@Override
	@Transactional(readOnly = true)
	public GetOrdersResponse getOrderGuest(GetGuestOrderHistoryRequest request) {
		Orders orders = ordersRepository.findByIdAndPhoneNumber(request.ordersId(), request.phoneNumber())
			.orElseThrow(() -> new NotFoundException(ErrorCode.ORDERS_NOT_FOUND));
		return GetOrdersResponse.form(orders);
	}
}
